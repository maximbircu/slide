package com.maximbircu.imageloader.api

data class SlideConfig(
    val memCacheSize: Int = 10 * 1024 * 1024, // 10MB
    val diskCacheTTL: Long = 4 * 60 * 60 * 1000L, // 4 hours in milliseconds
    val memCacheTTL: Long = 4 * 60 * 60 * 1000L // 4 hours in milliseconds
)
