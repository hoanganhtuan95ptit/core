package com.unknown.theme

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.unknown.coroutines.handler
import com.unknown.coroutines.launchCollect
import com.unknown.theme.provider.ThemeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

val appTheme by lazy {

    MutableSharedFlow<Map<String, Any>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

fun setupTheme(activity: FragmentActivity) = activity.lifecycleScope.launch(handler + Dispatchers.IO) {

    val map = ConcurrentHashMap<String, Any>()

    ThemeProvider.instant.launchCollect(this) {

        it.map { provider ->

            provider.provide(activity).launchCollect(this) { themeMap ->

                map.putAll(themeMap)
                appTheme.tryEmit(map)
            }
        }
    }
}