package com.one.coreapp.utils.extentions

import android.content.Context
import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun TextView.setColorRes(@ColorRes colorRes: Int) = setTextColor(ContextCompat.getColor(context, colorRes))

fun TextView.setText(res: Int, text: String): Boolean {
    text.takeIf {
        it.isNotBlank()
    }?.let {
        this.text = it
    } ?: res.takeIf {
        it > 0
    }?.let {
        this.setText(it)
    } ?: let {
        return false
    }

    return true
}

fun TextView.setHint(res: Int, text: String) {
    text.takeIf {
        it.isNotBlank()
    }?.let {
        this.hint = it
    } ?: res.takeIf {
        it > 0
    }?.let {
        this.setHint(it)
    }
}


abstract class Text<T>(val data: T) {

    abstract fun getString(context: Context): CharSequence
}

class TextStr(data: String) : Text<String>(data) {

    override fun getString(context: Context): CharSequence {
        return data
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextStr) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

class TextNumber(data: Number) : Text<Number>(data) {

    override fun getString(context: Context): CharSequence {
        return "$data"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextNumber) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

class TextRes : Text<Int> {

    var params: List<Text<*>> = emptyList()

    constructor(@StringRes data: Int, vararg params: Text<*>) : this(data, params.toList())

    constructor(@StringRes data: Int, params: List<Text<*>>) : super(data) {
        this.params = params
    }

    override fun getString(context: Context): CharSequence {
        return context.getString(data, *params.map { it.getString(context) }.toTypedArray())
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

class TextHtml : Text<Text<*>> {


    private var flags: Int = 0


    constructor(data: String) : this(TextStr(data))

    constructor(@StringRes data: Int, vararg params: Text<*>) : this(TextRes(data, *params))


    constructor(data: Text<*>) : this(data, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.FROM_HTML_MODE_COMPACT else 0)

    constructor(data: Text<*>, flags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.FROM_HTML_MODE_COMPACT else 0) : super(data) {

        this.flags = flags
    }


    override fun getString(context: Context): CharSequence {

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

fun Int.toText() = TextRes(this)

fun String.toText() = TextStr(this)

fun Number.toText() = TextNumber(this)


fun emptyText() = TextStr("")


fun TextView.setText(text: Any, goneWhenEmpty: Boolean = false, type: TextView.BufferType = TextView.BufferType.NORMAL) {

    val textStr = when (text) {
        is Int -> {
            context.getString(text)
        }
        is Text<*> -> {
            text.getString(context)
        }
        else -> {
            "$text"
        }
    }

    setText(textStr, type)

    setVisible(!(goneWhenEmpty && textStr.isBlank()))
}