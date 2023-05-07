@file:Suppress("UNCHECKED_CAST")

package com.one.coreapp.utils.extentions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.one.coreapp.App

fun Any.toBitmap(vararg transformations: Transformation<Bitmap>?): Bitmap =
    toBitmap(0, 0, *transformations)

fun Any.toBitmap(width: Int = 0, height: Int = 0, vararg transformations: Transformation<Bitmap>?): Bitmap =
    toBitmap(true, width, height, *transformations)

fun Any.toBitmap(cache: Boolean = true, width: Int = 0, height: Int = 0, vararg transformations: Transformation<Bitmap>?): Bitmap =
    if (width != 0 || height != 0) {
        get(cache, Glide.with(App.shared), *transformations)
            .submit(width, height)
            .get()
    } else {
        get(cache, Glide.with(App.shared), *transformations)
            .submit()
            .get()
    }

fun ImageView.setImage(source: Any, vararg transformations: Transformation<Bitmap>? = arrayOf(FitCenter()), withCrossFade: Boolean = true, cache: Boolean = true, placeHolderRes: Int? = null, errorRes: Int? = null) {

    val image = (source as? Image<*>)?.getImage(context) ?: source

    var requestBuilder: RequestBuilder<*> = if (image is Drawable) {

        Glide.with(this).asDrawable()
    } else {

        Glide.with(this).asBitmap()
    }

    if (errorRes != null) {

        requestBuilder = requestBuilder.error(errorRes)
    }

    if (placeHolderRes != null) {

        requestBuilder = requestBuilder.placeholder(placeHolderRes)
    }


    if (withCrossFade) requestBuilder = if (image is Drawable) {

        (requestBuilder as RequestBuilder<Drawable>).transition(DrawableTransitionOptions.withCrossFade())
    } else {

        (requestBuilder as RequestBuilder<Bitmap>).transition(BitmapTransitionOptions.withCrossFade())
    }


    requestBuilder = if (cache) {

        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false)
    } else {

        requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
    }


    requestBuilder.transform(MultiTransformation(*transformations)).load(image).into(this)
}


fun ImageView.load(cache: Boolean = true, source: Any, vararg transformations: Transformation<Bitmap>?) =
    load(cache, 0, 0, source, *transformations)

fun ImageView.load(width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
    load(Glide.with(this), width, height, source, *transformations)

fun ImageView.load(cache: Boolean = true, width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
    load(cache, Glide.with(this), width, height, source, *transformations)

fun ImageView.load(requestManager: RequestManager = Glide.with(this), width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
    load(true, requestManager, width, height, source, *transformations)

fun ImageView.load(cache: Boolean = true, requestManager: RequestManager = Glide.with(this), width: Int = 0, height: Int = 0, source: Any, vararg transformations: Transformation<Bitmap>?) =
    if (width != 0 || height != 0) {
        source.get(cache, requestManager, *transformations)
            .apply(RequestOptions().override(width, height))
            .into(this)
    } else {
        source.get(cache, requestManager, *transformations)
            .into(this)
    }


fun Any.get(requestManager: RequestManager, vararg transformations: Transformation<Bitmap>?): RequestBuilder<Bitmap> = get(true, requestManager, *transformations)

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


fun ImageView.clear() = Glide.with(this).clear(this)


abstract class Image<T : Any>(val data: T) {

    open fun getImage(context: Context): Any {
        return data
    }
}

class ImageStr(data: String) : Image<String>(data) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextStr) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

class ImageRes(data: Int) : Image<Int>(data) {

    override fun getImage(context: Context): Any {
        return ResourcesCompat.getDrawable(context.resources, data, context.theme)!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextRes) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

class ImageDrawable(data: Drawable) : Image<Drawable>(data) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImageDrawable) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}


fun Int.toImage() = ImageRes(this)

fun String.toImage() = ImageStr(this)

fun Drawable.toImage() = ImageDrawable(this)


fun emptyImage() = ImageStr("")