package com.simple.coreapp.utils.ext

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan


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