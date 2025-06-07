package com.simple.coreapp.ui.base.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simple.coreapp.utils.ext.setFullScreen

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFullScreen()

        super.onCreate(savedInstanceState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) window.setFullScreen()
    }
}