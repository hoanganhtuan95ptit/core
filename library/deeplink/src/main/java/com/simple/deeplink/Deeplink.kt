@file:Suppress("FoldInitializerAndIfToElvis")

package com.simple.deeplink

import android.view.View
import com.simple.deeplink.entities.DeeplinkData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun sendDeeplink(deepLink: String, extras: Map<String, Any?>? = null, sharedElement: Map<String, View>? = null) = CoroutineScope(Dispatchers.Main.immediate).launch {

    if (deepLink.isBlank()) {
        return@launch
    }


    val replayCache = flow.replayCache

    if (replayCache.isNotEmpty() && replayCache.any { !it.isHandled && it.deepLink.equals(deepLink, true) }) {
        return@launch
    }

    val data = DeeplinkData(
        deepLink = deepLink,
        extras = extras,
        sharedElement = sharedElement
    )

    flow.emit(data)
}
