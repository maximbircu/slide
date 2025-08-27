package com.maximbircu.imageloader.data.sources

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache
import com.maximbircu.imageloader.api.SlideConfig
import com.maximbircu.imageloader.data.caching.CacheExpiryTracker
import com.maximbircu.imageloader.engine.ImageKey

class MemoryImageSource(
    private val config: SlideConfig,
    private val expiryTracker: CacheExpiryTracker
) {
    private val memoryCache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(
        config.memCacheSize
    ) {
        override fun sizeOf(key: String, bitmap: Bitmap): Int = bitmap.byteCount
    }

    fun get(key: ImageKey): Bitmap? {
        try {
            val bitmap = memoryCache.get(key.toString())
            if (bitmap != null && expiryTracker.hasValid(key)) {
                return bitmap
            } else {
                memoryCache.remove(key.toString())
                expiryTracker.remove(key)
                return null
            }
        } catch (_: Exception) {
            return null
        }
    }

    fun put(key: ImageKey, bitmap: Bitmap) {
        memoryCache.put(key.toString(), bitmap)
        expiryTracker.recordPut(key, config.diskCacheTTL)
    }

    fun celar() {
        memoryCache.evictAll()
        expiryTracker.clear()
    }
}
