package com.simple.coreapp.utils.extentions.image

import android.content.Context
import java.lang.ref.WeakReference

abstract class Image {

    private var weakReference: WeakReference<Any>? = null

    open fun getImage(context: Context): Any {

        return weakReference?.get() ?: buildImage(context).apply {

            weakReference = WeakReference(this)
        }
    }

    protected abstract fun buildImage(context: Context): Any
}
