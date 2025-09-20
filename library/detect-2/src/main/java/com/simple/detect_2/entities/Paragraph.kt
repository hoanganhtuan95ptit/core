package com.simple.detect_2.entities

import android.graphics.Point
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Keep
@Parcelize
data class Paragraph(
    override var id: String = UUID.randomUUID().toString(),

    override var text: String = "",
    override var languageCode: String = "",

    override var points: List<Point>? = emptyList(),

    var words: List<Word> = emptyList(),
    var sentences: List<Sentence> = emptyList()
) : TextBlock(), Parcelable