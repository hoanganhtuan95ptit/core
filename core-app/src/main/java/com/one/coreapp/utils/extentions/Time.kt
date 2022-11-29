package com.one.coreapp.utils.extentions

import java.text.SimpleDateFormat
import java.util.*

fun Long.toTimeStr(format: String = "HH:mm"): String = SimpleDateFormat(format, Locale.US).apply {
    timeZone = TimeZone.getDefault()
}.format(Date(this))