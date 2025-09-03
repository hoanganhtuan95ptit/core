package com.unknown.theme

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.unknown.theme.provider.ThemeProvider
import com.unknown.coroutines.handler
import com.unknown.coroutines.launchCollect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

val appTheme by lazy {

    MutableSharedFlow<Map<String, Any>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

fun setupTheme(activity: FragmentActivity) = activity.lifecycleScope.launch(handler + Dispatchers.IO) {

    val map = ConcurrentHashMap<String, Any>()

    AutoBind.loadAsync(ThemeProvider::class.java, true).map { list ->

        list.sortedBy { it.priority() }
    }.launchCollect(this) {

        it.map { provider ->

            provider.provide(activity).launchCollect(this) { themeMap ->

                map.putAll(themeMap)
                appTheme.tryEmit(map)
            }
        }
    }
}
