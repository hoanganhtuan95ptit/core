//package com.tuanha.app.utils
//
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.text.Layout
//import android.text.StaticLayout
//import android.text.TextPaint
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
//import com.bumptech.glide.util.Util
//import com.one.detect.entities.TextBlock
//import com.one.detect.entities.wrap
//import java.nio.ByteBuffer
//import java.security.MessageDigest
//import kotlin.math.max
//
//class DrawTextTransformation(private val maxSize: Int, private val textBlock: List<TextBlock>) : BitmapTransformation() {
//
//    private val hashCode by lazy {
//        val text = textBlock.joinToString { it.textTranslate }
//
//        text.hashCode()
//    }
//
//    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
//
//        val bitmapResult: Bitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true)
//
//        val ratio = max(bitmapResult.height, bitmapResult.width) * 1f / maxSize
//
//        val canvas = Canvas(bitmapResult)
//
//        textBlock.filter { it.rect != null }.forEach {
//            var end = false
//
//            var textSize = 5f
//
//            val text = it.textTranslate.takeIf { it.isNotBlank() } ?: it.text
//
//            val bounds = it.rect!!.wrap(ratio)
//
//            while (!end) {
//                val textPaint: TextPaint = TextPaint().apply {
//                    this.textSize = textSize
//                }
//
//                val staticLayout = getStaticLayout(text, textPaint, bounds.width())
//
//                if (staticLayout.height > bounds.height()) {
//                    textSize -= 0.5f
//                    end = true
//                } else {
//                    textSize += 0.5f
//                }
//            }
//
//            val textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
//                this.textSize = textSize
//                this.color = Color.WHITE
//            }
//
//            val sl = getStaticLayout(text, textPaint, bounds.width())
//
//            canvas.save()
//
//            val textYCoordinate: Float = bounds.top.toFloat()
//            val textXCoordinate: Float = bounds.left.toFloat()
//            canvas.translate(textXCoordinate, textYCoordinate)
//            sl.draw(canvas)
//
//            canvas.restore()
//        }
//
//        return bitmapResult
//    }
//
//    private fun getStaticLayout(text: String, textPaint: TextPaint, width: Int): StaticLayout {
//        return StaticLayout(text, textPaint, max(width, 10), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true)
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || javaClass != other.javaClass) return false
//        val that = other as DrawTextTransformation
//        return textBlock == that.textBlock
//    }
//
//    override fun hashCode(): Int {
//        return Util.hashCode(ID.hashCode(), hashCode)
//    }
//
//    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//        messageDigest.update(ID_BYTES)
//
//        val radiusData = ByteBuffer.allocate(100).putInt(hashCode).array()
//        messageDigest.update(radiusData)
//    }
//
//    companion object {
//
//        private const val TAG = "DrawTextTransformation"
//
//        private val ID = DrawTextTransformation::class.java.name
//        private val ID_BYTES = ID.toByteArray(CHARSET)
//    }
//}