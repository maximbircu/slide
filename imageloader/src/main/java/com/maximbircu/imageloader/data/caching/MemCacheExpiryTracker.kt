package com.maximbircu.imageloader.data.caching

import com.maximbircu.imageloader.engine.ImageKey
import com.maximbircu.imageloader.infrastructure.TimeProvider
import java.util.concurrent.ConcurrentHashMap

class MemCacheExpiryTracker(
    private val timeProvider: TimeProvider
) : CacheExpiryTracker {
    private val expiryAt = ConcurrentHashMap<ImageKey, Long>()

    override fun recordPut(key: ImageKey, ttlMs: Long) {
        val now = timeProvider.nowMs
        expiryAt[key] = now + ttlMs
    }

    override fun hasValid(key: ImageKey): Boolean {
        val exp = expiryAt[key] ?: return false
        if (timeProvider.nowMs >= exp) {
            // remove only if unchanged to avoid races
            expiryAt.remove(key, exp)
            return false
        }
        return true
    }

    override fun remove(key: ImageKey) {
        expiryAt.remove(key)
    }

    override fun clear() = expiryAt.clear()
}
