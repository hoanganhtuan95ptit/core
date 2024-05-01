package com.simple.coreapp.utils.extentions.text

import android.content.Context
import android.text.style.BackgroundColorSpan
import com.simple.coreapp.utils.extentions.getColorFromAttr
import com.simple.coreapp.utils.extentions.text.span.BaseSpan

data class TextWithBackgroundColorAttrColor(val text: Text, val attrColor: Int) : Text() {

    override fun buildString(context: Context): CharSequence {

        return BaseSpan().addText(text.getString(context), BackgroundColorSpan(context.getColorFromAttr(attrColor)))
    }
}
