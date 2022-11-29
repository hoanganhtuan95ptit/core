package com.one.coreapp.utils.extentions

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater

fun LayoutInflater.cloneInContext(context: Context, theme: Int = -1): LayoutInflater {

    val contextThemeWrapper = if (theme > 0) {
        ContextThemeWrapper(context, theme)
    } else {
        context
    }

    return cloneInContext(contextThemeWrapper)
}