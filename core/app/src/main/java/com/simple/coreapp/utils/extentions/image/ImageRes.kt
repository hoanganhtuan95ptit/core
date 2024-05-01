package com.simple.coreapp.utils.extentions.image

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat


data class ImageRes(val data: Int) : Image() {

    override fun buildImage(context: Context): Drawable {

        return ResourcesCompat.getDrawable(context.resources, data, context.theme)!!
    }
}