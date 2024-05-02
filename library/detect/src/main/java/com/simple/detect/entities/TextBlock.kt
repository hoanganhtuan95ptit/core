package com.simple.detect.entities

import android.os.Parcelable
import androidx.annotation.Keep
import com.simple.core.utils.extentions.validate
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Keep
@Parcelize
open class TextBlock(
    open var id: String = UUID.randomUUID().toString(),

    open var text: String = "",
    open var languageCode: String = "",

    open var rect: TextRest? = null
) : Parcelable

fun <T : TextBlock> List<T>.wrap(ratio: Float) = validate {
    it.rect = it.rect?.wrap(ratio)
}
