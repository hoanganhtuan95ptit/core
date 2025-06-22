package com.simple.coreapp.utils.ext

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.TextView
import com.google.auto.service.AutoService
import java.util.ServiceLoader


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


fun emptyText() = RichText("")


fun String.toRich(): RichText {

    return RichText(this)
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

    spans.add(RichStyle(RichRange(start, end), arrayListOf(*spannable)))

    return refresh()
}


fun TextView.setText(text: RichText?) {

    setText(text?.textChar)
}


private val richSpanConvert by lazy {
    ServiceLoader.load(RichSpanConvert::class.java).toList()
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
                spannable.setSpan(style, span.range.start, span.range.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        textChar = spannable

        return this
    }

    private fun RichSpan.toAndroidSpan(): CharacterStyle {

        return richSpanConvert.firstNotNullOf { it.getAndroidSpan(this) }
    }
}

data class RichStyle(
    val range: RichRange,
    val styles: List<RichSpan> = arrayListOf()
)

data class RichRange(
    val start: Int,
    val end: Int
)


sealed class RichSpan

interface RichSpanConvert {
    fun getAndroidSpan(richSpan: RichSpan): CharacterStyle?
}


object Bold : RichSpan()

@AutoService(RichSpanConvert::class)
class BoldConvert : RichSpanConvert {

    override fun getAndroidSpan(richSpan: RichSpan): CharacterStyle? {
        return if (richSpan is Bold) StyleSpan(Typeface.BOLD) else null
    }
}


data class RelativeSize(val proportion: Float) : RichSpan()

@AutoService(RichSpanConvert::class)
class RelativeSizeConvert : RichSpanConvert {

    override fun getAndroidSpan(richSpan: RichSpan): CharacterStyle? {
        return if (richSpan is RelativeSize) RelativeSizeSpan(richSpan.proportion) else null
    }
}


data class ForegroundColor(val color: Int) : RichSpan()

@AutoService(RichSpanConvert::class)
class ForegroundColorConvert : RichSpanConvert {

    override fun getAndroidSpan(richSpan: RichSpan): CharacterStyle? {
        return if (richSpan is ForegroundColor) ForegroundColorSpan(richSpan.color) else null
    }
}