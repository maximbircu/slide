package com.maximbircu.slide

import android.app.Application
import com.maximbircu.imageloader.api.Slide

class SlideSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Slide.init(this)
    }
}
