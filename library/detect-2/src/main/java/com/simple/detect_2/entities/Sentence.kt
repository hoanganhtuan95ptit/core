package com.simple.detect_2.entities

import android.graphics.Point
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Keep
@Parcelize
data class Sentence(
    override var id: String = UUID.randomUUID().toString(),

    override var text: String = "",
    override var languageCode: String = "",

    override var points: List<Point>? = emptyList(),

    var angle: Float = 0f,
    var confidence: Float = 0f,

    var words: List<Word> = emptyList(),
) : TextBlock(), Parcelable