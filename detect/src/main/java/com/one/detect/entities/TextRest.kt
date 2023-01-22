package com.one.detect.entities

import android.os.Parcelable
import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class TextRest(var left: Int = 0, var top: Int = 0, var right: Int = 0, var bottom: Int = 0) : Parcelable {

    fun width() = right - left

    fun height() = bottom - top

    override fun toString(): String {
        return "$left - $top - $right - $bottom"
    }
}
