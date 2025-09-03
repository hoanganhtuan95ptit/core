package com.simple.deeplink.queue

import android.content.ComponentCallbacks
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.simple.deeplink.flow
import com.simple.deeplink.provider.DeeplinkProvider
import com.simple.deeplink.utils.exts.launchCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

abstract class DeeplinkQueue {

    abstract fun getQueue(): String

    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun setupDeepLink(componentCallbacks: ComponentCallbacks): Job? {

        val lifecycleOwner = (componentCallbacks as? ComponentActivity) ?: (componentCallbacks as? Fragment) ?: return null

        return flow.launchCollect(lifecycleOwner) { pair ->

            val deepLink = pair.first

            val extras = pair.second.first
            val sharedElement = pair.second.second

            val navigation = withContext(Dispatchers.IO) {

                val deeplinkProviders = AutoBind.load(DeeplinkProvider::class.java)

                val groupDeeplink = deeplinkProviders.flatMap {
                    it.provider()
                }.groupBy(keySelector = {
                    it.first
                }, valueTransform = {
                    it.second
                })

                groupDeeplink[getQueue()]?.find {

                    it.acceptDeeplink(componentCallbacks, deepLink)
                }
            }

            if (navigation?.navigation(componentCallbacks, deepLink, extras, sharedElement) == true) {

                flow.resetReplayCache()
            }
        }
    }
}
