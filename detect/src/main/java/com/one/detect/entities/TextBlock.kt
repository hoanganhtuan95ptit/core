package com.one.detect.entities

import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.one.core.utils.extentions.validate
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class TextBlock : Parcelable {

    val id: String = UUID.randomUUID().toString()

    var text: String = ""

    var textTranslate: String = ""

    var languageCode: String = ""

    var from: String = ""

    var rect: TextRest? = null

    @CallSuper
    open fun clearTextTranslate() {
        textTranslate = ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextBlock

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

fun <T : TextBlock> List<T>.wrap(ratio: Float) = validate {
    rect = rect?.wrap(ratio)
}
