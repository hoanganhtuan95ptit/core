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

fun ImageView.setImage(source: Any, vararg transformations: Transformation<Bitmap>) = when (source) {

    is GifImageData -> Glide.with(context).asGif()
        .transform(*transformations)
        .load(source.data)
        .into(this)

    is BitmapImageData -> Glide.with(context).asBitmap()
        .transform(*transformations)
        .load(source.data)
        .into(this)

    else -> Glide.with(context).asGif()
        .transform(*transformations)
        .load(source)
        .into(this)
}

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


fun emptyImage() = ImagePath("")

fun Int.toImage() = ImageRes(this)

fun String.toImage() = ImagePath(this)

fun Int.toImageGif() = GifImageRes(this)

fun String.toImageGif() = GifImagePath(this)


interface RichImage


abstract class RichImageData(open val data: Any) : RichImage


abstract class GifImageData(data: Any) : RichImageData(data)

data class GifImageRes(override val data: Int) : GifImageData(data)

data class GifImagePath(override val data: String) : GifImageData(data)


abstract class BitmapImageData(data: Any) : RichImageData(data)

data class ImageRes(override val data: Int) : BitmapImageData(data)

data class ImagePath(override val data: String) : BitmapImageData(data)
