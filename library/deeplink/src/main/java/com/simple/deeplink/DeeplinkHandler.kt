package com.simple.deeplink

import android.content.ComponentCallbacks
import android.view.View

interface DeeplinkHandler {

    fun getDeeplink(): String = ""

    suspend fun acceptDeeplink(deepLink: String): Boolean = deepLink == getDeeplink()

    suspend fun navigation(componentCallbacks: ComponentCallbacks, deepLink: String, extras: Map<String, Any?>?, sharedElement: Map<String, View>?): Boolean
}

