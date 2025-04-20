package com.simple.size.utils.exts

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal fun <T> Flow<T>.launchCollect(
    lifecycleOwner: LifecycleOwner,

    start: CoroutineStart = CoroutineStart.DEFAULT,
    context: CoroutineContext = EmptyCoroutineContext,

    collector: FlowCollector<T>
) = launchCollect(
    start = start,
    context = context,
    collector = collector,
    coroutineScope = lifecycleOwner.lifecycleScope
)

internal fun <T> Flow<T>.launchCollect(
    coroutineScope: CoroutineScope,

    start: CoroutineStart = CoroutineStart.DEFAULT,
    context: CoroutineContext = EmptyCoroutineContext,

    collector: FlowCollector<T>
) = coroutineScope.launch(start = start, context = context) {

    this@launchCollect.collect(collector)
}