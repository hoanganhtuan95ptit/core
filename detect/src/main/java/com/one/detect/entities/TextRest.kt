package com.one.detect.entities

import android.os.Parcelable
import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.parcelize.Parcelize
import kotlin.math.max
import kotlin.math.min

@Keep
@Parcelize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TextRest(var left: Int = 0, var top: Int = 0, var right: Int = 0, var bottom: Int = 0) : Parcelable {

    fun centerV() = top + height() / 2

    fun centerH() = left + width() / 2


    fun width() = right - left

    fun height() = bottom - top

    override fun toString(): String {
        return "$left - $top - $right - $bottom"
    }

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