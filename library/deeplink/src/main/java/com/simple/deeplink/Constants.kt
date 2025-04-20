package com.simple.deeplink

import android.view.View
import com.simple.deeplink.provider.DeeplinkProvider
import com.simple.deeplink.queue.DeeplinkQueue
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.ServiceLoader


internal val flow by lazy {

    MutableSharedFlow<Pair<String, Pair<Map<String, Any?>?, Map<String, View>?>>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

internal val groupDeeplink: Map<String, List<DeeplinkHandler>> by lazy {

    val plugins = ServiceLoader.load(DeeplinkProvider::class.java)

    plugins.flatMap {
        it.provider()
    }.groupBy(keySelector = {
        it.first
    }, valueTransform = {
        it.second
    })
}

internal val groupQueue: List<DeeplinkQueue> by lazy {

    val plugins = ServiceLoader.load(DeeplinkQueue::class.java)

    plugins.toList()
}
