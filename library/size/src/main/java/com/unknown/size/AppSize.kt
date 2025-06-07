package com.unknown.size

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.unknown.coroutines.handler
import com.unknown.coroutines.launchCollect
import com.unknown.size.provider.SizeProvider
import com.unknown.size.uitls.exts.getScreenHeight
import com.unknown.size.uitls.exts.getScreenWidth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap

data class Size(
    val width: Int = 0,
    val height: Int = 0,

    val heightStatusBar: Int = 0,
    val heightNavigationBar: Int = 0
) : ConcurrentHashMap<String, Int>()


val appSize by lazy {

    MutableSharedFlow<Size>(replay = 1, extraBufferCapacity = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.SUSPEND)
}

fun setupSize(activity: FragmentActivity) = activity.lifecycleScope.launch(handler + Dispatchers.IO) {

    val size = Size(
        width = getScreenWidth(activity),
        height = getScreenHeight(activity)
    )

    ServiceLoader.load(SizeProvider::class.java).toList().sortedBy { it.priority() }.map { provider ->

        provider.provide(activity).launchCollect(this) {

            size.putAll(it)
            appSize.tryEmit(size)
        }
    }
}