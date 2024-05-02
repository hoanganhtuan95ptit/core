package com.simple.detect.entities

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Keep
@Parcelize
data class Paragraph(
    override var id: String = UUID.randomUUID().toString(),

    override var text: String = "",
    override var languageCode: String = "",

    override var rect: TextRest? = null,

    var words: List<Word> = emptyList(),
    var sentences: List<Sentence> = emptyList()
) : TextBlock(
    id,
    text,
    languageCode,
    rect
)