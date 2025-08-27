package com.maximbircu.imageloader.engine.renderer

import android.graphics.Bitmap

interface ImageRenderer {
    fun onLoading()
    fun onLoaded(bitmap: Bitmap)
    fun onError()
}
