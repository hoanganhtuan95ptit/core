package com.simple.coreapp.utils.extentions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.toTimeStr(format: String = "HH:mm"): String = SimpleDateFormat(format, Locale.US).apply {
    timeZone = TimeZone.getDefault()
}.format(Date(this))