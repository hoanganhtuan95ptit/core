package com.simple.coreapp.utils.extentions

import android.widget.EditText
import android.widget.TextView


fun TextView.setText(text: Int, goneWhenEmpty: Boolean = false, type: TextView.BufferType = TextView.BufferType.NORMAL) {

    setText(context.getString(text), goneWhenEmpty, type)
}

fun TextView.setText(text: CharSequence, goneWhenEmpty: Boolean = false, type: TextView.BufferType? = TextView.BufferType.NORMAL) {

    if (type != null) {

        setText(text, type)
    } else {

        setText(text)
    }

    if (goneWhenEmpty) {

        setVisible(text.isNotBlank())
    }
}

fun TextView.setTextWhenDiff(textNew: CharSequence, goneWhenEmpty: Boolean = false): Boolean {

    return if (text.toString().equals(textNew.toString(), true)) {

        false
    } else {

        val last = text.length - selectionEnd

        setText(textNew, goneWhenEmpty)

        if (this is EditText && isFocused) kotlin.runCatching {

            setSelection(textNew.length - last)
        }

        true
    }
}