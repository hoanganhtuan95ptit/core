package com.simple.deeplink.queue

import android.content.ComponentCallbacks
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.simple.deeplink.flow
import com.simple.deeplink.groupDeeplink
import com.simple.deeplink.utils.exts.awaitResume
import com.simple.deeplink.utils.exts.launchCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

abstract class DeeplinkQueue {

    abstract fun getQueue(): String

    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun setupDeepLink(componentCallbacks: ComponentCallbacks) {

        val lifecycleOwner = (componentCallbacks as? ComponentActivity) ?: (componentCallbacks as? Fragment) ?: return

        flow.launchCollect(lifecycleOwner) { pair ->

            val deepLink = pair.first

            val extras = pair.second.first
            val sharedElement = pair.second.second

            val navigation = withContext(Dispatchers.IO) {

                groupDeeplink[getQueue()]?.find {

                    it.acceptDeeplink(componentCallbacks, deepLink)
                }
            }

            lifecycleOwner.awaitResume()

            if (navigation?.navigation(componentCallbacks, deepLink, extras, sharedElement) == true) {

                flow.resetReplayCache()
            }
        }
    }
}
