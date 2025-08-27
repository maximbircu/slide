package com.maximbircu.imageloader.infrastructure

import android.text.format.DateUtils.SECOND_IN_MILLIS

interface TimeProvider {
    val nowMs: Long
    val nowSec: Long

    object SystemTimeProvider : TimeProvider {
        override val nowMs get() = System.currentTimeMillis()
        override val nowSec get() = nowMs / SECOND_IN_MILLIS
    }
}
