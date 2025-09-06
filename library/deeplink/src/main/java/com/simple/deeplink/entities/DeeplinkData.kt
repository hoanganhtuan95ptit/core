package com.simple.deeplink.entities

import android.view.View

internal data class DeeplinkData(
    val deepLink: String,

    var extras: Map<String, Any?>? = null,
    var sharedElement: Map<String, View>? = null,

    var isHandled: Boolean = false
)