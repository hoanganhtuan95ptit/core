package com.unknown.size

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.simple.autobind.AutoBind
import com.unknown.coroutines.handler
import com.unknown.coroutines.launchCollect
import com.unknown.size.provider.SizeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

val appSize by lazy {

    MutableSharedFlow<Map<String, Int>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

fun setupSize(activity: FragmentActivity) = activity.lifecycleScope.launch(handler + Dispatchers.IO) {

    val size = ConcurrentHashMap<String, Int>()

    AutoBind.loadAsync(SizeProvider::class.java, true).map { list ->

        list.sortedBy { it.priority() }
    }.launchCollect(this) {

        it.map { provider ->

            provider.provide(activity).launchCollect(this) { sizeMap ->

                size.putAll(sizeMap)
                appSize.tryEmit(size)
            }
        }
    }
}