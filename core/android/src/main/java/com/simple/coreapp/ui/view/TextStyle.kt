package com.simple.coreapp.ui.view

import android.util.TypedValue
import android.widget.TextView

data class TextStyle(
    val typeface: Int? = null,
    val textSize: Float? = null,
    val textGravity: Int? = null
)

fun TextView.setTextStyle(textStyle: TextStyle? = null) {

    textStyle ?: return

    textStyle.typeface?.let {
        this.setTypeface(null, it)
    }

    textStyle.textSize?.let {
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, it)
    }

    textStyle.textGravity?.let {
        this.gravity = it
    }
}