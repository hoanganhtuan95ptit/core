package com.simple.image.exts

import android.content.Context

abstract class Image<T : Any>(open val data: T) {

    open fun getImage(context: Context): T {
        return data
    }
}