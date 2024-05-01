package com.simple.coreapp.utils.extentions.text

import android.content.Context

data class TextNumber(private val data: Number) : Text() {

    override fun buildString(context: Context): CharSequence {
        return "$data"
    }
}
