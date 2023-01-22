package com.one.detect.entities

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.one.core.utils.extentions.validate
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Paragraph : TextBlock() {

    var words: List<Word> = emptyList()

    var sentences: List<Sentence> = emptyList()

    override fun clearTextTranslate() {
        super.clearTextTranslate()
        sentences.validate { clearTextTranslate() }
    }
}