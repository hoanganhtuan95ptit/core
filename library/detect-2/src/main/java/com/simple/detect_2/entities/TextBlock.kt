package com.simple.detect_2.entities

import android.graphics.Point
import androidx.annotation.Keep
import kotlinx.parcelize.IgnoredOnParcel
import java.util.UUID

@Keep
abstract class TextBlock {

    open var id: String = UUID.randomUUID().toString()

    open var text: String = ""
    open var languageCode: String = ""

    open var points: List<Point>? = emptyList()
}
