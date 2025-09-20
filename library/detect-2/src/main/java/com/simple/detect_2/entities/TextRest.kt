package com.simple.detect_2.entities

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlin.math.max
import kotlin.math.min

@Keep
@Parcelize
data class TextRest(
    var left: Int = 0,
    var top: Int = 0,
    var right: Int = 0,
    var bottom: Int = 0
) : Parcelable {

    val centerV: Int
        get() = top + height / 2

    val centerH: Int
        get() = left + width / 2

    val width: Int
        get() = right - left

    val height: Int
        get() = bottom - top
}

fun TextRest.contains(r: TextRest): Boolean {

    return left < right && top < bottom && left <= r.left && top <= r.top && right >= r.right && bottom >= r.bottom
}

fun TextRest.add(r: TextRest) {
    left = min(left, r.left)
    top = min(top, r.top)
    right = max(right, r.right)
    bottom = max(bottom, r.bottom)
}

fun TextRest.wrap(ratio: Float) = TextRest().apply {
    left = (ratio * this@wrap.left).toInt()
    top = (ratio * this@wrap.top).toInt()
    right = (ratio * this@wrap.right).toInt()
    bottom = (ratio * this@wrap.bottom).toInt()
}