package com.one.coreapp.utils.extentions

import android.util.Size
import kotlin.math.roundToInt

fun Size.rotate(rotate: Double): Size = rotate(rotate.roundToInt())

fun Size.rotate(rotate: Int): Size = when (rotate / 90 % 4 % 2) {
    0 -> Size(width, height)
    else -> Size(height, width)
}