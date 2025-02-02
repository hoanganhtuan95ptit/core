package com.simple.coreapp.utils.extentions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

fun Bitmap.filter(colorFilter: ColorFilter): Bitmap {
    val paint = Paint().apply {
        this.colorFilter = colorFilter
    }

    val resultBitmap = Bitmap.createBitmap(width, height, config)

    Canvas(resultBitmap).drawBitmap(this, 0f, 0f, paint)

    return resultBitmap
}

fun Bitmap.filter(vararg colorMatrix: ColorMatrix): Bitmap = filter(ColorMatrixColorFilter(ColorMatrix().apply {
    colorMatrix.forEach {
        postConcat(it)
    }
}))