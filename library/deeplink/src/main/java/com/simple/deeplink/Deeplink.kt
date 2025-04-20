@file:Suppress("FoldInitializerAndIfToElvis")

package com.simple.deeplink

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun sendDeeplink(deepLink: String, extras: Map<String, Any?>? = null, sharedElement: Map<String, View>? = null) = CoroutineScope(Dispatchers.Main.immediate).launch {

    if (!flow.replayCache.toMap().containsKey(deepLink)) {

        flow.emit(deepLink to (extras to sharedElement))
    }
}
