//@file:Suppress("UNCHECKED_CAST")
//
//package com.simple.coreapp.utils.extentions
//
//import android.graphics.Bitmap
//import android.graphics.drawable.Drawable
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.RequestBuilder
//import com.bumptech.glide.RequestManager
//import com.bumptech.glide.load.MultiTransformation
//import com.bumptech.glide.load.Transformation
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
//import com.bumptech.glide.load.resource.bitmap.FitCenter
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.bumptech.glide.request.RequestOptions
//
//fun Any.toBitmap(vararg transformations: Transformation<Bitmap>?): Bitmap =
//    toBitmap(0, 0, *transformations)
//
//fun Any.toBitmap(width: Int = 0, height: Int = 0, vararg transformations: Transformation<Bitmap>?): Bitmap =
//    toBitmap(true, width, height, *transformations)
//
//fun Any.toBitmap(cache: Boolean = true, width: Int = 0, height: Int = 0, vararg transformations: Transformation<Bitmap>?): Bitmap =
//    if (width != 0 || height != 0) {
//        get(cache, Glide.with(BaseApp.shared), *transformations)
//            .submit(width, height)
//            .get()
//    } else {
//        get(cache, Glide.with(BaseApp.shared), *transformations)
//            .submit()
//            .get()
//    }
//
//
//fun ImageView.load(cache: Boolean = true, source: Any, vararg transformations: Transformation<Bitmap>?) =
//    load(cache, 0, 0, source, *transformations)
//
//fun ImageView.load(width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
//    load(Glide.with(this), width, height, source, *transformations)
//
//fun ImageView.load(cache: Boolean = true, width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
//    load(cache, Glide.with(this), width, height, source, *transformations)
//
//fun ImageView.load(requestManager: RequestManager = Glide.with(this), width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
//    load(true, requestManager, width, height, source, *transformations)
//
//fun ImageView.load(cache: Boolean = true, requestManager: RequestManager = Glide.with(this), width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
//    if (width != 0 || height != 0) {
//        source.get(cache, requestManager, *transformations)
//            .apply(RequestOptions().override(width, height))
//            .into(this)
//    } else {
//        source.get(cache, requestManager, *transformations)
//            .into(this)
//    }
//
//
//fun Any.get(requestManager: RequestManager, vararg transformations: Transformation<Bitmap>?): RequestBuilder<Bitmap> = get(true, requestManager, *transformations)
//
//fun Any.get(cache: Boolean = true, requestManager: RequestManager, vararg transformations: Transformation<Bitmap>?): RequestBuilder<Bitmap> = if (cache) requestManager.asBitmap()
//    .transform(if (transformations.isEmpty()) FitCenter() else MultiTransformation(*transformations))
//    .transition(BitmapTransitionOptions.withCrossFade())
//    .load(this)
//else requestManager.asBitmap()
//    .transform(if (transformations.isEmpty()) FitCenter() else MultiTransformation(*transformations))
//    .diskCacheStrategy(DiskCacheStrategy.NONE)
//    .skipMemoryCache(true)
//    .transition(BitmapTransitionOptions.withCrossFade())
//    .load(this)
//
//
//fun ImageView.clear() = Glide.with(this).clear(this)
//
//
//fun ImageView.setImage(image: String, vararg transformations: Transformation<Bitmap>? = arrayOf(FitCenter()), withCrossFade: Boolean = true, cache: Boolean = true, placeHolderRes: Int? = null, errorRes: Int? = null) {
//
//
//    var requestBuilder = Glide.with(this).asBitmap()
//
//
//    if (withCrossFade) requestBuilder = requestBuilder.transition(BitmapTransitionOptions.withCrossFade())
//
//
//    setImage(builder = requestBuilder, transformations = transformations, cache = cache, placeHolderRes = placeHolderRes, errorRes = errorRes) { it.load(image) }
//}
//
//fun ImageView.setImage(image: Bitmap, vararg transformations: Transformation<Bitmap>? = arrayOf(FitCenter()), withCrossFade: Boolean = true, cache: Boolean = true, placeHolderRes: Int? = null, errorRes: Int? = null) {
//
//
//    var requestBuilder = Glide.with(this).asBitmap()
//
//
//    if (withCrossFade) requestBuilder = requestBuilder.transition(BitmapTransitionOptions.withCrossFade())
//
//
//    setImage(builder = requestBuilder, transformations = transformations, cache = cache, placeHolderRes = placeHolderRes, errorRes = errorRes) { it.load(image) }
//}
//
//fun ImageView.setImage(image: Drawable, vararg transformations: Transformation<Bitmap>? = arrayOf(FitCenter()), withCrossFade: Boolean = true, cache: Boolean = true, placeHolderRes: Int? = null, errorRes: Int? = null) {
//
//    var requestBuilder = Glide.with(this).asDrawable()
//
//
//    if (withCrossFade) requestBuilder = requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
//
//
//    setImage(builder = requestBuilder, transformations = transformations, cache = cache, placeHolderRes = placeHolderRes, errorRes = errorRes) { it.load(image) }
//}
//
//private fun ImageView.setImage(
//    builder: RequestBuilder<*>,
//    vararg transformations: Transformation<Bitmap>? = arrayOf(FitCenter()),
//    cache: Boolean = true,
//    placeHolderRes: Int? = null,
//    errorRes: Int? = null,
//    load: (RequestBuilder<*>) -> RequestBuilder<*>
//) {
//
//    var requestBuilder = builder
////
////
////    if (errorRes != null) {
////
////        requestBuilder = requestBuilder.error(errorRes)
////    }
////
////
////    if (placeHolderRes != null) {
////
////        requestBuilder = requestBuilder.placeholder(placeHolderRes)
////    }
////
////
////    requestBuilder = if (cache) {
////
////        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false)
////    } else {
////
////        requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
////    }
////
////
//    requestBuilder = requestBuilder.transform(MultiTransformation(*transformations))
//
//    requestBuilder = load(requestBuilder)
//
//    requestBuilder.into(this)
//}