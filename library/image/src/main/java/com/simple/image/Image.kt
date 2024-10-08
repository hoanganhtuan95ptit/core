@file:Suppress("UNCHECKED_CAST")

package com.simple.image

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.simple.image.exts.Image

//fun Any.toBitmap(vararg transformations: Transformation<Bitmap>?): Bitmap =
//    toBitmap(0, 0, *transformations)
//
//fun Any.toBitmap(width: Int = 0, height: Int = 0, vararg transformations: Transformation<Bitmap>?): Bitmap =
//    toBitmap(true, width, height, *transformations)
//

fun Any.toBitmap(context: Context, width: Int, height: Int): Bitmap = Glide.with(context).asBitmap()
    .diskCacheStrategy(DiskCacheStrategy.NONE)
    .skipMemoryCache(true)
    .load(this)
    .submit(width, height)
    .get()

fun Any.toBitmap(context: Context, width: Int, height: Int, vararg transformations: Transformation<Bitmap>): Bitmap = Glide.with(context).asBitmap()
    .diskCacheStrategy(DiskCacheStrategy.NONE)
    .skipMemoryCache(true)
    .transform(*transformations)
    .load(this)
    .submit(width, height)
    .get()

fun Any.toBitmap(context: Context, vararg transformations: Transformation<Bitmap>): Bitmap = Glide.with(context).asBitmap()
    .diskCacheStrategy(DiskCacheStrategy.NONE)
    .skipMemoryCache(true)
    .transform(*transformations)
    .load(this)
    .submit()
    .get()


fun ImageView.setImage(source: Int) =
    setImageResource(source)

fun ImageView.setImage(source: String) = Glide.with(context).asBitmap()
    .load(source)
    .into(this)

fun ImageView.setImage(source: String, vararg transformations: Transformation<Bitmap>) = Glide.with(context).asBitmap()
    .transform(*transformations)
    .load(source)
    .into(this)


fun ImageView.setImage(source: Image<*>) = Glide.with(context).asBitmap()
    .load(source.getImage(context))
    .into(this)

fun ImageView.setImage(source: Image<*>, vararg transformations: Transformation<Bitmap>) = Glide.with(context).asBitmap()
    .transform(*transformations)
    .load(source.getImage(context))
    .into(this)

fun ImageView.setImage(source: String, onLoadFailed: () -> Unit, onResourceReady: (Bitmap?) -> Unit, vararg transformations: Transformation<Bitmap>) = Glide.with(context).asBitmap()
    .addListener(object : RequestListener<Bitmap> {

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
            onLoadFailed()
            return false
        }

        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            onResourceReady(resource)
            return false
        }
    })
    .transform(*transformations)
    .load(source)

//
//fun ImageView.setImage(source: Image<*>, withCrossFade: Boolean = true, cache: Boolean = true, placeHolderRes: Int? = null, errorRes: Int? = null) {
//
//    val image = (source as? Image<*>)?.getImage(context) ?: source
//
//    var requestBuilder: RequestBuilder<*> = if (image is Drawable) {
//
//        Glide.with(this).asDrawable()
//    } else {
//
//        Glide.with(this).asBitmap()
//    }
//
//    if (errorRes != null) {
//
//        requestBuilder = requestBuilder.error(errorRes)
//    }
//
//    if (placeHolderRes != null) {
//
//        requestBuilder = requestBuilder.placeholder(placeHolderRes)
//    }
//
//
//    if (withCrossFade) requestBuilder = if (image is Drawable) {
//
//        (requestBuilder as RequestBuilder<Drawable>).transition(DrawableTransitionOptions.withCrossFade())
//    } else {
//
//        (requestBuilder as RequestBuilder<Bitmap>).transition(BitmapTransitionOptions.withCrossFade())
//    }
//
//
//    requestBuilder = if (cache) {
//
//        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false)
//    } else {
//
//        requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//    }
//
//
//    requestBuilder.transform(MultiTransformation(*transformations)).load(image).into(this)
//}
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
fun Any.get(cache: Boolean = true, requestManager: RequestManager, vararg transformations: Transformation<Bitmap>?): RequestBuilder<Bitmap> = if (cache) requestManager.asBitmap()
    .transform(if (transformations.isEmpty()) FitCenter() else MultiTransformation(*transformations))
    .transition(BitmapTransitionOptions.withCrossFade())
    .load(this)
else requestManager.asBitmap()
    .transform(if (transformations.isEmpty()) FitCenter() else MultiTransformation(*transformations))
    .diskCacheStrategy(DiskCacheStrategy.NONE)
    .skipMemoryCache(true)
    .transition(BitmapTransitionOptions.withCrossFade())
    .load(this)
//
//
//fun ImageView.clear() = Glide.with(this).clear(this)
//
//
//abstract class Image<T : Any>(val data: T) {
//
//    open fun getImage(context: Context): Any {
//        return data
//    }
//}
//
//class ImageStr(data: String) : Image<String>(data) {
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other !is TextStr) return false
//
//        if (data != other.data) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        return data.hashCode()
//    }
//}
//
//class ImageRes(data: Int) : Image<Int>(data) {
//
//    override fun getImage(context: Context): Any {
//        return ResourcesCompat.getDrawable(context.resources, data, context.theme)!!
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other !is TextRes) return false
//
//        if (data != other.data) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        return data.hashCode()
//    }
//}
//
//class ImageDrawable(data: Drawable) : Image<Drawable>(data) {
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other !is ImageDrawable) return false
//
//        if (data != other.data) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        return data.hashCode()
//    }
//}
//
//
//fun Int.toImage() = ImageRes(this)
//
//fun String.toImage() = ImageStr(this)
//
//fun Drawable.toImage() = ImageDrawable(this)
//
//
//fun emptyImage() = ImageStr("")