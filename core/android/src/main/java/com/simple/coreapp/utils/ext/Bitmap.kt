package com.simple.coreapp.utils.ext

import android.graphics.Bitmap

fun Bitmap.scaleBitmapDown(maxDimension: Int): Bitmap {

    val originalWidth = width
    val originalHeight = height

    var resizedWidth = maxDimension
    var resizedHeight = maxDimension

    if (originalHeight > originalWidth) {
        resizedHeight = maxDimension
        resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
    } else if (originalWidth > originalHeight) {
        resizedWidth = maxDimension
        resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
    } else if (originalHeight == originalWidth) {
        resizedHeight = maxDimension
        resizedWidth = maxDimension
    }

    return Bitmap.createScaledBitmap(this, resizedWidth, resizedHeight, false)
}