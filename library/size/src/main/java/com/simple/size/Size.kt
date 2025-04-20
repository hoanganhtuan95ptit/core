package com.simple.size

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.simple.size.entities.Size
import com.simple.size.utils.exts.getScreenHeight
import com.simple.size.utils.exts.getScreenWidth
import com.simple.size.utils.exts.launchCollect
import com.simple.size.utils.exts.listenerOnHeightStatusAndHeightNavigationChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlin.coroutines.coroutineContext


private val sizeFlow by lazy {

    MutableSharedFlow<Size>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}


internal fun ComponentActivity.setupSize() = channelFlow<Unit> {

    val activity = this@setupSize


    val size = Size(
        width = getScreenWidth(activity),
        height = getScreenHeight(activity)
    )
    sizeFlow.tryEmit(size)


    activity.listenerOnHeightStatusAndHeightNavigationChange().launchCollect(this) {

        val sizeCopy = size.copy(
            heightStatusBar = it.first,
            heightNavigationBar = it.second
        )
        sizeFlow.tryEmit(sizeCopy)
    }

    awaitClose {

    }
}.launchIn(this.lifecycleScope)


suspend fun listenerSize(block: suspend (data: Size) -> Unit) {

    listenerSize(coroutineScope = CoroutineScope(coroutineContext), block = block)
}

fun listenerSize(lifecycle: Lifecycle, block: suspend (data: Size) -> Unit) {

    listenerSize(coroutineScope = lifecycle.coroutineScope, block = block)
}

fun listenerSize(coroutineScope: CoroutineScope, block: suspend (data: Size) -> Unit) {

    sizeFlow.launchCollect(coroutineScope = coroutineScope) {

        block(it)
    }
}