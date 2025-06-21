package com.simple.coreapp.utils.ext

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView


fun String.boldWith(bold: String): CharSequence {

    return (this as CharSequence).with(bold, StyleSpan(Typeface.BOLD))
}

fun String.with(vararg spannable: Any): CharSequence {

    return with(this, *spannable)
}

fun String.with(bold: String, vararg spannable: Any): CharSequence {

    val spannableString = SpannableString(this)

    val start = indexOf(bold)
    if (start < 0) return spannableString

    val end = start + bold.length
    if (end > length) return spannableString

    spannable.forEach {

        spannableString.setSpan(it, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return spannableString
}


fun CharSequence.with(vararg spannable: Any): CharSequence {

    return with(this, *spannable)
}

fun CharSequence.with(bold: CharSequence, vararg spannable: Any): CharSequence {

    val spannableString = SpannableString(this)

    val start = indexOf(bold.toString())
    if (start < 0) return spannableString

    val end = start + bold.length
    if (end > length) return spannableString

    spannable.forEach {

        spannableString.setSpan(it, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return spannableString
}


fun String.with(vararg spannable: RichSpan): RichText {

    return RichText(this).with(this, *spannable)
}

fun String.with(bold: String, vararg spannable: RichSpan): RichText {

    return RichText(this).with(bold, *spannable)
}

fun RichText.with(vararg spannable: RichSpan): RichText {

    return with(this.text, *spannable)
}

fun RichText.with(bold: String, vararg spannable: RichSpan): RichText {

    val start = text.indexOf(bold)
    if (start < 0) return this

    val end = start + bold.length
    if (end > text.length) return this

    spans.add(RichStyle(IntRange(start, end), arrayListOf(*spannable)))

    return refresh()
}


fun TextView.setText(text: RichText) {

    setText(text.textChar)
}


data class RichText(
    val text: String,
    val spans: ArrayList<RichStyle> = arrayListOf()
) {

    var textChar: CharSequence = text

    init {
        refresh()
    }

    fun refresh(): RichText {

        val spannable = SpannableString(text)
        spans.forEach { span ->
            span.styles.forEach { styleData ->
                val style = styleData.toAndroidSpan()
                spannable.setSpan(style, span.range.first, span.range.last + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        textChar = spannable

        return this
    }

    private fun RichSpan.toAndroidSpan(): CharacterStyle = when (this) {
        is RichSpan.Bold -> StyleSpan(Typeface.BOLD)
        is RichSpan.ForegroundColor -> ForegroundColorSpan(color)
    }
}

data class RichStyle(
    val range: IntRange,
    val styles: List<RichSpan> = arrayListOf()
)

sealed class RichSpan {

    object Bold : RichSpan()

    data class ForegroundColor(val color: Int) : RichSpan()
}