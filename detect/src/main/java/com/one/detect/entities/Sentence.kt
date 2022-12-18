package com.one.detect.entities

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.one.detect.data.entities.TextBlock
import com.one.detect.data.entities.Word


@Keep
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Sentence : TextBlock() {

    var words: List<Word> = emptyList()

}