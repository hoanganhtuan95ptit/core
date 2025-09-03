package com.simple.deeplink

import android.view.View
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

internal val flow by lazy {

    MutableSharedFlow<Pair<String, Pair<Map<String, Any?>?, Map<String, View>?>>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}