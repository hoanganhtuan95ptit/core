package com.simple.detect.entities

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Keep
@Parcelize
data class Sentence(
    override var id: String = UUID.randomUUID().toString(),

    override var text: String = "",
    override var languageCode: String = "",

    override var rect: TextRest? = null,

    var words: List<Word> = emptyList()
) : TextBlock(
    id,
    text,
    languageCode,
    rect
)