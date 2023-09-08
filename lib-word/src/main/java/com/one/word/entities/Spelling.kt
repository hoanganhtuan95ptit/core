package com.one.word.entities

import android.os.Parcelable
import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Spelling constructor(
    var code: String = "",
    var text: String = "",
    var audio: String = "",
) : Parcelable
