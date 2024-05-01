package com.simple.coreapp.utils.extentions.text

import android.content.Context
import com.simple.coreapp.utils.extentions.text.span.BaseSpan

class TextSpan : Text {

    private var text: Text

    private var params: Array<Any>

    constructor(text: Text, param: Any) : this(text, *arrayOf(param))

    constructor(text: Text, params: List<Any>) : this(text, *params.toTypedArray())

    constructor(text: Text, vararg params: Any) : super() {

        this.text = text

        this.params = arrayOf(*params)
    }

    override fun buildString(context: Context): CharSequence {

        return BaseSpan().addText(text.getString(context), *params)
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (other !is TextSpan) return false

        if (text != other.text) return false
        if (!params.contentEquals(other.params)) return false

        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }
}
