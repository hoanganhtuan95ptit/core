//package com.simple.image
//
//import android.graphics.Bitmap
//import android.graphics.drawable.Drawable
//import android.widget.ImageView
//import com.bumptech.glide.load.Transformation
//import com.bumptech.glide.load.resource.bitmap.FitCenter
//import com.simple.image.exts.Image
//
//
//fun Int.toImage() = ImageRes(this)
//
//fun String.toImage() = ImageStr(this)
//
//fun Drawable.toImage() = ImageDrawable(this)
//
//
//private val imageEmpty = ImageStr("EMPTY")
//
//fun emptyImage() = imageEmpty
//
//
//fun ImageView.setImage(image: Image, vararg transformations: Transformation<Bitmap>? = arrayOf(FitCenter()), withCrossFade: Boolean = true, cache: Boolean = true, placeHolderRes: Int? = null, errorRes: Int? = null) {
//
//    when (val imageWrap = image.getImage(context)) {
//
//        is Bitmap -> setImage(image = imageWrap, transformations = transformations, withCrossFade = withCrossFade, cache = cache, placeHolderRes = placeHolderRes, errorRes = errorRes)
//
//        is String -> setImage(image = imageWrap, transformations = transformations, withCrossFade = withCrossFade, cache = cache, placeHolderRes = placeHolderRes, errorRes = errorRes)
//
//        is Drawable -> setImage(image = imageWrap, transformations = transformations, withCrossFade = withCrossFade, cache = cache, placeHolderRes = placeHolderRes, errorRes = errorRes)
//    }
//}