package com.simple.coreapp.utils.ext

import android.content.Intent

fun Intent.getStringOrEmpty(key: String): String {

    return getStringExtra(key) ?: ""
}