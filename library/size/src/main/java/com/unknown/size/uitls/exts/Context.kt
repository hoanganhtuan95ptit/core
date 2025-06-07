package com.unknown.size.uitls.exts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi


// --- Screen Width ---
fun Context.screenWidth(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        screenWidthModern()
    } else {
        screenWidthLegacy()
    }

@Suppress("DEPRECATION")
private fun Context.screenWidthLegacy(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Context.screenWidthModern(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.currentWindowMetrics.bounds.width()
}

// --- Screen Height ---
fun Context.screenHeight(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        screenHeightModern()
    } else {
        screenHeightLegacy()
    }

@Suppress("DEPRECATION")
private fun Context.screenHeightLegacy(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Context.screenHeightModern(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.currentWindowMetrics.bounds.height()
}

// --- Status Bar Height ---
fun Context.statusBarHeight(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        statusBarHeightModern()
    } else {
        statusBarHeightLegacy()
    }

@SuppressLint("DiscouragedApi", "InternalInsetResource")
private fun Context.statusBarHeightLegacy(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Context.statusBarHeightModern(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = wm.currentWindowMetrics
    val insets = metrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())
    return insets.top
}

// --- Navigation Bar Height ---
fun Context.navigationBarHeight(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        navigationBarHeightModern()
    } else {
        navigationBarHeightLegacy()
    }

@SuppressLint("DiscouragedApi", "InternalInsetResource")
private fun Context.navigationBarHeightLegacy(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Context.navigationBarHeightModern(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val metrics = wm.currentWindowMetrics
    val insets = metrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())
    return insets.bottom
}