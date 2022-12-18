package com.one.detect.entities

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

@Keep
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class TextRest(var left: Int = 0, var top: Int = 0, var right: Int = 0, var bottom: Int = 0) : Serializable {
    fun width() = right - left

    fun height() = bottom - top

    override fun toString(): String {
        return "$left - $top - $right - $bottom"
    }
}
