package com.simple.coreapp.utils.extentions.text

import android.content.Context
import com.simple.coreapp.utils.extentions.text.span.BaseSpan

data class TextMulti(val params: List<Text>, val separator: CharSequence = " ") : Text() {

    override fun buildString(context: Context): CharSequence {

        var span = BaseSpan()

        params.forEachIndexed { index, text ->

            span = span.addText(text.getString(context))

            if (index < params.size - 1) span = span.addText(separator)
        }

        return span
    }
}
