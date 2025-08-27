package com.maximbircu.imageloader.engine.renderer

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.scale
import com.maximbircu.imageloader.engine.ScaleType

class ImageViewRenderer(
    private val imageView: ImageView,
    private val loadingPlaceholderResId: Int? = null,
    private val failurePlaceholderResId: Int? = null,
) : ImageRenderer {
    private val imageViewLoader: ImageViewLoader = ImageViewLoader()

    override fun onLoading() {
        loadingPlaceholderResId?.let { imageView.setImageDrawable(getDrawable(it)) }
    }

    override fun onLoaded(bitmap: Bitmap) {
        imageViewLoader.load(imageView, bitmap)
    }

    override fun onError() {
        (failurePlaceholderResId ?: loadingPlaceholderResId)?.let {
            imageView.setImageDrawable(getDrawable(it))
        }
    }

    private fun getDrawable(@DrawableRes res: Int): Drawable? {
        return AppCompatResources.getDrawable(imageView.context, res)
    }
}

class ImageViewLoader() {
    fun load(imageView: ImageView, bitmap: Bitmap) {
        imageView.afterMeasured {
            val targetWidth: Int? = imageView.width
            val targetHeight: Int? = imageView.height
            val scaleType: ScaleType = when (imageView.scaleType) {
                ImageView.ScaleType.CENTER_CROP -> ScaleType.CENTER_CROP
                else -> ScaleType.FIT
            }

            imageView.setImageBitmap(scaleBitmap(bitmap, targetWidth, targetHeight, scaleType))
        }
    }

    fun scaleBitmap(
        bitmap: Bitmap,
        targetWidth: Int? = null,
        targetHeight: Int? = null,
        scaleType: ScaleType = ScaleType.FIT
    ): Bitmap {
        return if (targetWidth != null && targetHeight != null &&
            (bitmap.width > targetWidth || bitmap.height > targetHeight)
        ) {
            when (scaleType) {
                ScaleType.FIT -> {
                    bitmap.scale(targetWidth, targetHeight)
                }
                ScaleType.CENTER_CROP -> {
                    val aspectRatioBitmap = bitmap.width.toFloat() / bitmap.height
                    val aspectRatioTarget = targetWidth.toFloat() / targetHeight
                    var cropWidth = bitmap.width
                    var cropHeight = bitmap.height
                    if (aspectRatioBitmap > aspectRatioTarget) {
                        cropWidth = (bitmap.height * aspectRatioTarget).toInt()
                    } else {
                        cropHeight = (bitmap.width / aspectRatioTarget).toInt()
                    }
                    val x = (bitmap.width - cropWidth) / 2
                    val y = (bitmap.height - cropHeight) / 2
                    val cropped = Bitmap.createBitmap(bitmap, x, y, cropWidth, cropHeight)
                    cropped.scale(targetWidth, targetHeight)
                }
            }
        } else {
            bitmap
        }
    }

    inline fun <T : View> T.afterMeasured(crossinline function: T.() -> Unit) {
        if (isLaidOut) {
            function()
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (viewTreeObserver.isAlive && measuredWidth > 0 && measuredHeight > 0) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                            function()
                        }
                    }
                }
            )
        }
    }
}
