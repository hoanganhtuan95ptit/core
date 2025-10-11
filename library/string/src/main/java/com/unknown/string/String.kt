package com.unknown.string

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.unknown.coroutines.handler
import com.unknown.coroutines.launchCollect
import com.unknown.string.provider.StringProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap


val appString by lazy {

    MutableSharedFlow<Map<String, String>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

fun setupString(activity: FragmentActivity) = activity.lifecycleScope.launch(handler + Dispatchers.IO) {

    val map = ConcurrentHashMap<String, String>()

    StringProvider.instant.launchCollect(this) {

        it.map { provider ->

            provider.provide(activity).launchCollect(this) { stringMap ->

                map.putAll(stringMap)
                appString.tryEmit(map)
            }
        }
    }
}

