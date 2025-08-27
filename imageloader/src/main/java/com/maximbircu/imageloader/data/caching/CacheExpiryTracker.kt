package com.maximbircu.imageloader.data.caching

import com.maximbircu.imageloader.engine.ImageKey

interface CacheExpiryTracker {
    fun recordPut(key: ImageKey, ttlMs: Long)
    fun hasValid(key: ImageKey): Boolean
    fun remove(key: ImageKey)
    fun clear()
}
