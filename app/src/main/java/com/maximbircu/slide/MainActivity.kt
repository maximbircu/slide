package com.maximbircu.slide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maximbircu.slide.presentation.ImageListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hide action bar since we're using a custom toolbar
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ImageListFragment())
                .commit()
        }
    }
}
