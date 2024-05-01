package com.simple.coreapp.utils.extentions

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat


fun Context.getBooleanFromAttr(@AttrRes attrBoolean: Int): Boolean {
    val typedArray: TypedArray = obtainStyledAttributes(null, listOf(attrBoolean).toIntArray())

    return try {
        typedArray.getBoolean(0, false)
    } finally {
        typedArray.recycle()
    }
}


fun Context.getResourceFromAttr(@AttrRes attrRes: Int): Int {
    val typedArray = obtainStyledAttributes(null, listOf(attrRes).toIntArray())

    return try {
        typedArray.getResourceId(0, 0)
    } finally {
        typedArray.recycle()
    }
}

fun Context.getColorFromAttr(@AttrRes attrColor: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrColor, typedValue, true)
    return typedValue.data
}

@ColorInt
fun Context.getColorFromAttr(@AttrRes attrColor: Int, typedValue: TypedValue = TypedValue(), resolveRefs: Boolean = true): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.allPermissionsGranted(vararg permission: String) = allPermissionsGranted(permission.toList())

fun Context.allPermissionsGranted(permissions: List<String>): Boolean {
    return !(Build.VERSION.SDK_INT >= 23 && permissions.any { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED })
}


fun Context.vibrate() {

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {

        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @SuppressLint("MissingPermission")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val audioAttributes = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)

        vibrator.vibrate(vibrationEffect, audioAttributes)
    } else {

        @Suppress("DEPRECATION")
        vibrator.vibrate(500)
    }
}