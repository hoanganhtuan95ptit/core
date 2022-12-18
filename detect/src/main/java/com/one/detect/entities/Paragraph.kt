package com.one.detect.entities

import com.one.core.utils.extentions.validate
import com.one.detect.data.entities.TextBlock
import com.one.detect.data.entities.Word
import com.one.detect.entities.Sentence

class Paragraph : TextBlock() {

    var words: List<Word> = emptyList()

    var sentences: List<Sentence> = emptyList()

    override fun clearTextTranslate() {
        super.clearTextTranslate()
        sentences.validate { clearTextTranslate() }
    }
}