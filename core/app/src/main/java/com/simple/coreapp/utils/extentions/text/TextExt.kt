@file:Suppress("PackageDirectoryMismatch")

package com.simple.coreapp.utils.extentions

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.widget.TextView
import com.simple.coreapp.utils.extentions.text.Text
import com.simple.coreapp.utils.extentions.text.TextMulti
import com.simple.coreapp.utils.extentions.text.TextNumber
import com.simple.coreapp.utils.extentions.text.TextRes
import com.simple.coreapp.utils.extentions.text.TextSpan
import com.simple.coreapp.utils.extentions.text.TextStr
import com.simple.coreapp.utils.extentions.text.TextWithTextColorAttrColor


fun Int.toText() = TextRes(this)

fun String.toText() = TextStr(this)

fun Number.toText() = TextNumber(this)

fun List<Text>.toText(separator: CharSequence = " ") = TextMulti(this, separator)


fun Text.withStyle(type: Int = Typeface.BOLD) = TextSpan(this, StyleSpan(type))

fun Text.withTextColor(attrColor: Int) = TextWithTextColorAttrColor(this, attrColor)


private val textEmpty = TextStr("")

fun emptyText() = textEmpty


fun TextView.setText(text: Text, goneWhenEmpty: Boolean = false, type: TextView.BufferType = TextView.BufferType.NORMAL) {

    setText(text.getString(context), goneWhenEmpty, type)
}


fun TextView.setTextWhenDiff(text: Text, goneWhenEmpty: Boolean = false): Boolean {

    return setTextWhenDiff(text.getString(context), goneWhenEmpty)
}