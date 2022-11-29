@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.one.coreapp.utils.extentions

import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.max

suspend fun <T> ProducerScope<T>.offerActiveAwait(t: T, start: Long = -1, max: Long = -1) {

    if (start > 0 && max > 0) {
        delay(max(0, max - System.currentTimeMillis() + start))
    }

    offerActive(t)

    awaitClose { }
}

fun <T> ProducerScope<T>.offerActive(t: T) {

    if (isActive) trySend(t)
}
