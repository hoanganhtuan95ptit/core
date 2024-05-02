package com.simple.coreapp.utils.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.simple.core.utils.extentions.resumeActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext


internal val map = mapOf(
    Lifecycle.State.CREATED to Lifecycle.Event.ON_CREATE,
    Lifecycle.State.STARTED to Lifecycle.Event.ON_START,
    Lifecycle.State.RESUMED to Lifecycle.Event.ON_RESUME,
    Lifecycle.State.DESTROYED to Lifecycle.Event.ON_DESTROY
)


suspend fun awaitState(state: Lifecycle.State) = withContext(Dispatchers.Main.immediate) {

    val lifecycle = ProcessLifecycleOwner.get().lifecycle

    if (lifecycle.currentState === state) {

        return@withContext
    }

    var lifecycleEventObserver: LifecycleEventObserver? = null

    try {

        suspendCancellableCoroutine<Boolean> { continuation ->

            val eventAwait = map[state]

            lifecycleEventObserver = LifecycleEventObserver { _, event ->

                if (event == eventAwait) {

                    continuation.resumeActive(true)
                }
            }

            lifecycle.addObserver(lifecycleEventObserver!!)
        }
    } finally {

        lifecycleEventObserver?.let { lifecycle.removeObserver(it) }
    }
}


suspend fun awaitCreated() = awaitState(Lifecycle.State.CREATED)

suspend fun awaitStarted() = awaitState(Lifecycle.State.STARTED)

suspend fun awaitResumed() = awaitState(Lifecycle.State.RESUMED)

suspend fun awaitDestroyed() = awaitState(Lifecycle.State.DESTROYED)


