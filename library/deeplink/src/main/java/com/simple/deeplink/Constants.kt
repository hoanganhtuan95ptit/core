package com.simple.deeplink

import com.simple.deeplink.entities.DeeplinkData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

internal val flow by lazy {

    MutableSharedFlow<DeeplinkData>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}