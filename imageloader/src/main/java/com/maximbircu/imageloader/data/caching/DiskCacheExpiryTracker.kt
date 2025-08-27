package com.maximbircu.imageloader.data.caching

import android.content.Context
import androidx.core.content.edit
import com.maximbircu.imageloader.engine.ImageKey

class DiskCacheExpiryTracker(context: Context) : CacheExpiryTracker {
    private val prefs = context.getSharedPreferences("bitmap_expiry", Context.MODE_PRIVATE)

    override fun recordPut(key: ImageKey, ttlMs: Long) {
        val expiryAt = System.currentTimeMillis() + ttlMs
        prefs.edit { putLong(key.toString(), expiryAt) }
    }

    override fun hasValid(key: ImageKey): Boolean {
        val expiryAt = prefs.getLong(key.toString(), -1L)
        if (expiryAt == -1L) return false
        val now = System.currentTimeMillis()
        val valid = now < expiryAt
        if (!valid) remove(key)
        return valid
    }

    override fun remove(key: ImageKey) {
        prefs.edit { remove(key.toString()) }
    }

    override fun clear() {
        prefs.edit { clear() }
    }
}
