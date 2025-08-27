package com.maximbircu.imageloader.data.sources

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.maximbircu.imageloader.api.SlideConfig
import com.maximbircu.imageloader.data.caching.CacheExpiryTracker
import com.maximbircu.imageloader.engine.ImageKey
import java.io.File
import java.io.FileOutputStream

class DiskImageSource(
    context: Context,
    private val config: SlideConfig,
    private val cacheExpiryTracker: CacheExpiryTracker
) {
    private val diskCacheDir: File = File(context.cacheDir, "image_cache").also {
        if (!it.exists()) it.mkdirs()
    }

    fun get(key: ImageKey): Bitmap? {
        val diskFile = File(diskCacheDir, key.toString())
        if (diskFile.exists() && cacheExpiryTracker.hasValid(key)) {
            try {
                return BitmapFactory.decodeFile(diskFile.absolutePath)
            } catch (e: Exception) {
                // If there's an error reading from disk, delete the file
                diskFile.delete()
                return null
            }
        }
        return null
    }

    fun put(key: ImageKey, bitmap: Bitmap) {
        val diskFile = File(diskCacheDir, key.toString())
        FileOutputStream(diskFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        cacheExpiryTracker.recordPut(key, config.diskCacheTTL)
    }

    fun hasValid(key: ImageKey): Boolean = cacheExpiryTracker.hasValid(key)

    fun clear() {
        diskCacheDir.listFiles()?.forEach { it.delete() }
        cacheExpiryTracker.clear()
    }
}
