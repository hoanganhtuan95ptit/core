package com.simple.detect.entities

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Keep
@Parcelize
data class Word(
    override var id: String = UUID.randomUUID().toString(),

    override var text: String = "",
    override var languageCode: String = "",

    override var rect: TextRest? = null
) : TextBlock(
    id,
    text,
    languageCode,
    rect
)