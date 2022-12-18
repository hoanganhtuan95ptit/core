package com.one.detect.entities

import androidx.annotation.CallSuper
import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import java.util.*

@Keep
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class TextBlock : Serializable {

    val id: String = UUID.randomUUID().toString()

    var text: String = ""

    var textTranslate: String = ""

    var languageCode: String = ""

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
