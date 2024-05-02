//package com.simple.image
//
//import android.content.Context
//import android.graphics.drawable.Drawable
//import androidx.core.content.res.ResourcesCompat
//import com.simple.image.exts.Image
//
//
//data class ImageRes(val data: Int) : Image() {
//
//    override fun buildImage(context: Context): Drawable {
//
//        return ResourcesCompat.getDrawable(context.resources, data, context.theme)!!
//    }
//}