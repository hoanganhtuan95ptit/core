package com.tuanha.app.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest

class AlphaTransformation(private val alpha: Int) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmapResult: Bitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(bitmapResult)

        val bitmapOverlay = Bitmap.createBitmap(bitmapResult.width, bitmapResult.height, bitmapResult.config).apply {
            eraseColor(Color.BLACK)
        }

        canvas.drawBitmap(bitmapOverlay, 0f, 0f, Paint(Paint.FILTER_BITMAP_FLAG).apply {
            this.alpha = this@AlphaTransformation.alpha
        })

        bitmapOverlay.recycle()

        return bitmapResult
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AlphaTransformation
        return alpha == that.alpha
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode(), alpha)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)

        val radiusData = ByteBuffer.allocate(100).putInt(alpha).array()
        messageDigest.update(radiusData)
    }

    companion object {
        private val ID = AlphaTransformation::class.java.name
        private val ID_BYTES = ID.toByteArray(CHARSET)
    }
}