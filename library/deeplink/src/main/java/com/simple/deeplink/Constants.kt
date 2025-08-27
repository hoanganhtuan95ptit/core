package com.simple.deeplink

import android.view.View
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.simple.deeplink.provider.DeeplinkProvider
import com.simple.deeplink.queue.DeeplinkQueue
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow


internal val flow by lazy {

    MutableSharedFlow<Pair<String, Pair<Map<String, Any?>?, Map<String, View>?>>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

internal val groupDeeplink: Map<String, List<DeeplinkHandler>> by lazy {

    val plugins = AutoBind.load(DeeplinkProvider::class.java, false)

    plugins.flatMap {
        it.provider()
    }.groupBy(keySelector = {
        it.first
    }, valueTransform = {
        it.second
    })
}

internal val groupQueue: List<DeeplinkQueue> by lazy {

    val plugins = AutoBind.load(DeeplinkQueue::class.java, true)

    plugins.toList()
}
