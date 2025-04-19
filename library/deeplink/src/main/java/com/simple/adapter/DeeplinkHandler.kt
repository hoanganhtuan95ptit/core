package com.simple.adapter

import android.content.ComponentCallbacks
import android.os.Bundle
import android.view.View

interface DeeplinkHandler {

    fun getDeeplink(): String = ""

    suspend fun acceptDeeplink(deepLink: String): Boolean = deepLink == getDeeplink()

    suspend fun navigation(componentCallbacks: ComponentCallbacks, deepLink: String, extras: Bundle?, sharedElement: Map<String, View>?): Boolean
}

