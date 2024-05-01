package com.simple.coreapp.utils.extentions.text

import android.content.Context
import android.os.Build
import android.text.Html
import androidx.annotation.StringRes

class TextHtml : Text {


    private val data: Text

    private var flags: Int = 0


    constructor(data: String) : this(TextStr(data))

    constructor(@StringRes data: Int, vararg params: Text) : this(TextRes(data, *params))

    constructor(data: Text) : this(data, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.FROM_HTML_MODE_COMPACT else 0)

    constructor(data: Text, flags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.FROM_HTML_MODE_COMPACT else 0) : super() {

        this.data = data
        this.flags = flags
    }


    override fun buildString(context: Context): CharSequence {

        val text = data.getString(context).toString()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Html.fromHtml(text, flags)
        } else {

            Html.fromHtml(text)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextHtml) return false

        if (data != other.data) return false

        return true
    }


    override fun hashCode(): Int {
        return data.hashCode()
    }
}
