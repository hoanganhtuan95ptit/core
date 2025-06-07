package com.unknown.size

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.unknown.coroutines.handler
import com.unknown.coroutines.launchCollect
import com.unknown.size.provider.SizeProvider
import com.unknown.size.uitls.exts.getOrZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap

class Size : ConcurrentHashMap<String, Int>() {

    val width: Int
        get() = getOrZero("width")

    val height: Int
        get() = getOrZero("height")

    val heightStatusBar: Int
        get() = getOrZero("heightStatusBar")

    val heightNavigationBar: Int
        get() = getOrZero("heightNavigationBar")
}


val appSize by lazy {

    MutableSharedFlow<Size>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

fun setupSize(activity: FragmentActivity) = activity.lifecycleScope.launch(handler + Dispatchers.IO) {

    val size = Size()

    ServiceLoader.load(SizeProvider::class.java).toList().sortedBy { it.priority() }.map { provider ->

        provider.provide(activity).launchCollect(this) {

            size.putAll(it)
            appSize.tryEmit(size)
        }
    }
}