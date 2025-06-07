package com.unknown.size

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.unknown.coroutines.handler
import com.unknown.coroutines.launchCollect
import com.unknown.size.provider.SizeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap

val appSize by lazy {

    MutableSharedFlow<Map<String, Int>>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

fun setupSize(activity: FragmentActivity) = activity.lifecycleScope.launch(handler + Dispatchers.IO) {

    val size = ConcurrentHashMap<String, Int>()

    ServiceLoader.load(SizeProvider::class.java).toList().sortedBy { it.priority() }.map { provider ->

        provider.provide(activity).launchCollect(this) {

            size.putAll(it)
            appSize.tryEmit(size)
        }
    }
}