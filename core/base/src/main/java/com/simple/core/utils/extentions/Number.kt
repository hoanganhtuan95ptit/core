package com.simple.core.utils.extentions

fun Int?.orDefault(value: Int) = this ?: value

fun Int?.orZero() = this ?: 0


fun Float?.orDefault(value: Float) = this ?: value

fun Float?.orZero() = this ?: 0f


fun Double?.orDefault(value: Double) = this ?: value

fun Double?.orZero() = this ?: 0.0