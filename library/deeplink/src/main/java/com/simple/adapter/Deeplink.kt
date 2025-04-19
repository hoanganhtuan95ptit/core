package com.simple.adapter

import android.content.ComponentCallbacks
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.withResumed
import com.simple.core.utils.extentions.asObjectOrNull
import com.simple.coreapp.utils.ext.launchCollect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val list: List<DeeplinkHandler> by lazy {

    val className = "com.tuanha.deeplink.DeeplinkProvider"
    val clazz = Class.forName(className)

    val method = clazz.getMethod("all")
    (method.invoke(null) as List<*>).filterIsInstance<DeeplinkHandler>()
}


abstract class DeeplinkQueueHandler {

    protected val flow by lazy {

        MutableSharedFlow<Pair<String, Pair<Bundle?, Map<String, View>?>>>(replay = 0, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun setupDeepLink(componentCallbacks: ComponentCallbacks) {

        val lifecycleOwner = componentCallbacks.asObjectOrNull<ComponentActivity>() ?: componentCallbacks.asObjectOrNull<Fragment>() ?: return

        flow.launchCollect(lifecycleOwner) { pair ->

            lifecycleOwner.awaitResume()

            val deepLink = pair.first

            val extras = pair.second.first
            val sharedElement = pair.second.second

            val navigation = withContext(Dispatchers.IO) {

                list.find { it.acceptDeeplink(deepLink) }
            }

            if (navigation?.navigation(componentCallbacks, deepLink, extras, sharedElement) == true) {

                flow.resetReplayCache()
            }
        }
    }

    fun sendDeeplink(deepLink: String, extras: Bundle? = null, sharedElement: Map<String, View>? = null) {

        CoroutineScope(Dispatchers.Main.immediate).launch {

            if (!flow.replayCache.toMap().containsKey(deepLink)) {

                flow.emit(deepLink to (extras to sharedElement))
            }
        }
    }

    private suspend fun LifecycleOwner.awaitResume() = channelFlow {

        withResumed {
            trySend(Unit)
        }

        awaitClose {

        }
    }.first()
}