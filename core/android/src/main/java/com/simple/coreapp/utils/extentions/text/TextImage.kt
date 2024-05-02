package com.simple.coreapp.utils.extentions.text

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.simple.coreapp.utils.extentions.text.span.BaseSpan
import com.simple.coreapp.utils.extentions.text.span.VerticalImageSpan

data class TextImage(val image: Int, val size: Int) : Text() {

    override fun buildString(context: Context): CharSequence {

        val drawable = ResourcesCompat.getDrawable(context.resources, image, context.theme)!!
        drawable.setBounds(0, 0, size, size)

        return BaseSpan().addText(" ", VerticalImageSpan(drawable))
    }
}
