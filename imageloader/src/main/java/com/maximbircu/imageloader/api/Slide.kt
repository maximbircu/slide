package com.maximbircu.imageloader.api

import android.content.Context
import android.widget.ImageView
import com.maximbircu.imageloader.data.sources.DiskImageSource
import com.maximbircu.imageloader.data.ImageRepository
import com.maximbircu.imageloader.data.sources.MemoryImageSource
import com.maximbircu.imageloader.data.sources.NetworkImageSource
import com.maximbircu.imageloader.data.caching.DiskCacheExpiryTracker
import com.maximbircu.imageloader.data.caching.MemCacheExpiryTracker
import com.maximbircu.imageloader.engine.ImageKey
import com.maximbircu.imageloader.engine.ImageLoadingClient
import com.maximbircu.imageloader.engine.renderer.ImageViewRenderer
import com.maximbircu.imageloader.engine.ScaleType
import com.maximbircu.imageloader.infrastructure.TimeProvider.SystemTimeProvider

object Slide {
    @Volatile
    lateinit var client: ImageLoadingClient
        private set

    /**
     * Loads an image from the provided URL into the given ImageView.
     *
     * @param url The URL of the image to load.
     * @param imageView The ImageView where the image will be displayed.
     * @param loadingPlaceholderResId Optional resource ID for a placeholder image to show while loading.
     * @param failurePlaceholderResId Optional resource ID for a placeholder image to show if loading fails.
     */
    @JvmStatic
    fun load(
        url: String,
        imageView: ImageView,
        loadingPlaceholderResId: Int? = null,
        failurePlaceholderResId: Int? = null
    ) {
        client.load(
            key = ImageKey(url, imageView.tag?.toString() ?: ""),
            target = ImageViewRenderer(
                imageView,
                loadingPlaceholderResId,
                failurePlaceholderResId
            )
        )
    }

    /**
     * Invalidates memory and disk cache for the whole library.
     */
    @JvmStatic
    fun invalidateCache() {
        client.invalidateCache()
    }

    fun init(context: Context, config: SlideConfig = SlideConfig()) {
        client = ImageLoadingClient(
            repository = ImageRepository(
                memorySource = MemoryImageSource(config, MemCacheExpiryTracker(SystemTimeProvider)),
                diskSource = DiskImageSource(context, config, DiskCacheExpiryTracker(context)),
                networkSource = NetworkImageSource()
            )
        )
    }
}
