package com.simple.coreapp.utils.extentions.image

import android.content.Context
import android.graphics.drawable.Drawable

data class ImageDrawable(val data: Drawable) : Image() {

    override fun getImage(context: Context): Drawable {

        return data
    }

    override fun buildImage(context: Context): Drawable {

        return data
    }
}