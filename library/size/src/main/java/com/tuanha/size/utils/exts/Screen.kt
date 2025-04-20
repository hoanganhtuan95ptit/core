package com.tuanha.size.utils.exts

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import androidx.annotation.NonNull

internal fun getScreenHeight(@NonNull context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.y
}

internal fun getScreenWidth(@NonNull context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}