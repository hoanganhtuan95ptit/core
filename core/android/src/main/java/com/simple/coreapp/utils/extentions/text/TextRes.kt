package com.simple.coreapp.utils.extentions.text

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.annotation.StringRes

class TextRes : Text {

    private val data: Int

    private var params: List<Text>

    constructor(@StringRes data: Int, vararg params: Text) : this(data, params.toList())

    constructor(@StringRes data: Int, params: List<Text>) : super() {

        this.data = data
        this.params = params
    }

    override fun buildString(context: Context): CharSequence {

        val string = context.getString(data, *params.map { "%s" }.toTypedArray())

        if (params.isEmpty()) {

            return string
        }

        val spannableString = SpannableStringBuilder(string)

        params.forEach {

            val charSequence = it.getString(context)

            val start = spannableString.indexOf("%s").takeIf { it >= 0 } ?: return@forEach
            val end = start + 2

            spannableString.replace(start, end, charSequence)
        }

        return spannableString
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextRes) return false

        if (data != other.data) return false
        if (params != other.params) return false

        return true
    }

    override fun hashCode(): Int {
        return params.hashCode()
    }
}
