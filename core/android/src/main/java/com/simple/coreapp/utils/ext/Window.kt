package com.simple.coreapp.utils.ext

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


fun Window.setFullScreen() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

        this.setupFullScreenApi31()
    } else {

        this.setupFullScreenApi30()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Window.setupFullScreenApi31() {

    WindowCompat.setDecorFitsSystemWindows(this, false)

    val windowInsetsController = WindowCompat.getInsetsController(this, this.decorView)
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    this.setDecorFitsSystemWindows(false)

    this.decorView.setOnApplyWindowInsetsListener { view, insets ->

        view.setPadding(0, 0, 0, 0)

        insets
    }
}

@Suppress("DEPRECATION")
private fun Window.setupFullScreenApi30() {

    this.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    this.statusBarColor = Color.TRANSPARENT
    this.navigationBarColor = Color.TRANSPARENT
}
