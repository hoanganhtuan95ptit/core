package com.simple.detect.entities

import android.graphics.Point
import androidx.annotation.Keep
import com.simple.core.utils.extentions.validate
import kotlinx.parcelize.IgnoredOnParcel
import java.util.UUID

@Keep
abstract class TextBlock {

    open var id: String = UUID.randomUUID().toString()

    open var text: String = ""
    open var languageCode: String = ""

    open var points: List<Point>? = emptyList()

    @IgnoredOnParcel
    @Transient
    var extraData: HashMap<String, Any> = hashMapOf()
}
