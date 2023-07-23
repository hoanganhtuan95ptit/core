package com.four.job

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.startup.Initializer
import com.one.core.utils.extentions.resumeActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

internal class JobQueueInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {

            awaitDestroyed()

            JobQueueManager.cancel()
        }

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}


suspend fun awaitDestroyed() = withContext(Dispatchers.Main.immediate) {

    val lifecycle = ProcessLifecycleOwner.get().lifecycle

    if (lifecycle.currentState === Lifecycle.State.DESTROYED) {

        return@withContext
    }

    var lifecycleEventObserver: LifecycleEventObserver? = null

    try {

        suspendCancellableCoroutine<Boolean> { continuation ->

            lifecycleEventObserver = LifecycleEventObserver { a, event ->

                if (event == Lifecycle.Event.ON_DESTROY) {

                    continuation.resumeActive(true)
                }
            }

            lifecycle.addObserver(lifecycleEventObserver!!)
        }
    } finally {

        lifecycleEventObserver?.let { lifecycle.removeObserver(it) }
    }
}
