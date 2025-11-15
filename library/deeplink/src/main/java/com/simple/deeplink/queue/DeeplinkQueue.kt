package com.simple.deeplink.queue

import android.content.ComponentCallbacks
import androidx.lifecycle.LifecycleOwner
import com.simple.autobind.AutoBind
import com.simple.deeplink.flow
import com.simple.deeplink.provider.DeeplinkProvider
import com.simple.deeplink.utils.exts.getLifecycleOwner
import com.unknown.coroutines.launchCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

abstract class DeeplinkQueue {

    abstract fun getQueue(): String

    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun setupDeepLink(componentCallbacks: ComponentCallbacks): Job? {

        return setupDeepLink(componentCallbacks, componentCallbacks.getLifecycleOwner() ?: return null)
    }

    private fun setupDeepLink(componentCallbacks: ComponentCallbacks, lifecycleOwner: LifecycleOwner): Job? = flow.launchCollect(lifecycleOwner) { data ->

        val deepLink = data.deepLink

        val extras = data.extras
        val sharedElement = data.sharedElement

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

        if (navigation == null || data.isHandled) {

            return@launchCollect
        }

        if (!navigation.navigation(componentCallbacks, deepLink, extras, sharedElement)) {

            return@launchCollect
        }

        data.isHandled = true

        data.extras = null
        data.sharedElement = null
    }
}
