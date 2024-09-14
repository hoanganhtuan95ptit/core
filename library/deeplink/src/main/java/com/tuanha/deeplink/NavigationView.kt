package com.tuanha.deeplink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private val appDeeplink = MutableSharedFlow<Pair<String, Bundle?>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)


fun sendDeeplink(deepLink: String, vararg pair: Pair<String, Any>) {

    sendDeeplink(deepLink, bundleOf(*pair))
}

fun sendDeeplink(deepLink: String, extras: Bundle? = null) = CoroutineScope(Dispatchers.Main.immediate).launch {

    if (!appDeeplink.replayCache.toMap().containsKey(deepLink)) {

        appDeeplink.emit(deepLink to extras)
    }
}

private fun LifecycleOwner.launchRepeatOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch {

    lifecycle.repeatOnLifecycle(state, block)
}

private fun <T> Flow<T>.launchCollect(
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

private fun <T> Flow<T>.launchCollect(
    coroutineScope: CoroutineScope,

    start: CoroutineStart = CoroutineStart.DEFAULT,
    context: CoroutineContext = EmptyCoroutineContext,

    collector: FlowCollector<T>
) = coroutineScope.launch(start = start, context = context) {

    this@launchCollect.collect(collector)
}


private val list: List<DeepLinkHandle> by lazy {

    DeeplinkManager.navigation()
}


interface NavigationView {

    fun setupNavigation(activity: ComponentActivity)
}

class NavigationViewImpl : NavigationView {

    override fun setupNavigation(activity: ComponentActivity) {

        var job: Job? = null

        activity.launchRepeatOnLifecycle(Lifecycle.State.RESUMED) {

            job?.cancel()

            job = appDeeplink.launchCollect(activity) {

                val deepLink = it.first
                val extras = it.second

                val navigation = withContext(Dispatchers.IO) {

                    list.find { it.acceptDeeplink(deepLink) }
                }

                if (navigation?.navigation(activity, deepLink, extras) == true) {

                    appDeeplink.resetReplayCache()
                }
            }
        }
    }
}
