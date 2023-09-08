package com.one.word.entities

import android.os.Parcelable
import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.one.word.entities.TextLevelType.Companion.toTextLevelType
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TextLevel(
    val text: String = "",
) : Parcelable {

    @JsonProperty("level")
    private var levelInt: Int = TextLevelType.H1.value

    var level: TextLevelType
        @JsonIgnore get() {
            return levelInt.toTextLevelType()
        }
        @JsonIgnore set(value) {
            levelInt = value.value
        }
}

enum class TextLevelType(val value: Int) {

    H1(0), H2(1), H3(2), H4(3);

    companion object {

        fun Int.toTextLevelType() = values().find { it.value == this }!!
    }
}