package com.one.coreapp.entities

import android.os.Parcelable
import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Notification constructor(): Parcelable{

    val id: String = ""

    val title: String = ""

    val messages: List<String> = emptyList()

    val languageCode: String = ""

    val timeRanges: List<String> = emptyList()
}