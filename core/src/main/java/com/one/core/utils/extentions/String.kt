package com.one.core.utils.extentions

import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.regex.Pattern

private const val NORMALIZE_PATTER = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+"

private val SPECIAL: Pattern = Pattern.compile("[^ !#\$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~0123456789]")

private val NUMBER_REGEX = "[0-9]+".toRegex()

fun String?.normalize(): String {
    if (isNullOrBlank()) return ""

    val preNormalizeInput = this.lowercase().replace("đ", "d")

    val unicodeFormNormalize = Normalizer.normalize(preNormalizeInput, Normalizer.Form.NFKD)

    return String(unicodeFormNormalize.replace(NORMALIZE_PATTER, "").toByteArray(charset(Charsets.US_ASCII.name())), Charsets.US_ASCII)
        .replace("?", "")
}


fun String.hasNumber() = this.contains(NUMBER_REGEX)

fun String.hasChar() = containsChar()

fun String.containsChar() = SPECIAL.matcher(this).find()

@Suppress("SimpleDateFormat")
fun String.dateToLong(pattern: String = "yyyy-MM-dd"): Long {

    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.parse(this).time
}