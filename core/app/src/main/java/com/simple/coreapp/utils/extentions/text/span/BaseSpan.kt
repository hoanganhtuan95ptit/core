package com.simple.coreapp.utils.extentions.text.span

import android.text.SpannableStringBuilder

class BaseSpan : SpannableStringBuilder() {

    fun addText(text: CharSequence, vararg spans: Any): BaseSpan {

        append(text)

        for (span in spans) {
            setSpan(span, length - text.length, length, SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return this
    }
}
