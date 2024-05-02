package com.simple.coreapp.utils.extentions.text

import android.content.Context
import java.lang.ref.WeakReference

abstract class Text {

    private var weakReference: WeakReference<CharSequence>? = null

    open fun getString(context: Context): CharSequence {

        return weakReference?.get() ?: buildString(context).apply {

            weakReference = WeakReference(this)
        }
    }

    protected abstract fun buildString(context: Context): CharSequence
}
