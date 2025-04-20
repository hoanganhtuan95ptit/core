@file:Suppress("DEPRECATION")

package com.simple.size.utils.exts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentCallbacks
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.simple.core.utils.extentions.asObject
import com.simple.core.utils.extentions.asObjectOrNull
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull

internal fun WindowInsets.getStatusBar() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    getInsets(WindowInsets.Type.systemBars()).top
} else {
    systemWindowInsetTop
}

@SuppressLint("InternalInsetResource")
internal fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}


internal fun WindowInsets.getNavigationBar() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    getInsets(WindowInsets.Type.navigationBars()).bottom
} else {
    systemWindowInsetBottom
}

@SuppressLint("InternalInsetResource")
internal fun getNavigationBarHeight(context: Context): Int {
    val resources = context.resources
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        resources.getDimensionPixelSize(resourceId)
    } else 0
}

internal fun ComponentCallbacks.doOnHeightStatusAndHeightNavigationChange(onChange: suspend (heightStatusBar: Int, heightNavigationBar: Int) -> Unit) {

    val lifecycleOwner: LifecycleOwner = this.asObjectOrNull<Fragment>()?.viewLifecycleOwner ?: this.asObjectOrNull<AppCompatActivity>() ?: return

    listenerOnHeightStatusAndHeightNavigationChange().launchCollect(lifecycleOwner) {

        onChange(it.first, it.second)
    }
}

internal fun ComponentCallbacks.listenerOnHeightStatusAndHeightNavigationChange() = listenerOnApplyWindowInsetsAsync().mapNotNull { insets ->

    val activity = getActivity()
    val heightStatusBar = insets.getStatusBar().takeIf { it >= 10 } ?: getStatusBarHeight(activity)
    val heightNavigationBar = insets.getNavigationBar().takeIf { it >= 10 } ?: getNavigationBarHeight(activity)

    if (heightStatusBar <= 0 || heightNavigationBar <= 0) {
        null
    } else {
        Pair(heightStatusBar, heightNavigationBar)
    }
}.distinctUntilChanged()

private fun ComponentCallbacks.listenerOnApplyWindowInsetsAsync() = channelFlow {

    val activity = getActivity()

    val decorView = activity.window?.decorView
        ?: activity.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)
        ?: this@listenerOnApplyWindowInsetsAsync.asObjectOrNull<Fragment>()?.view
        ?: return@channelFlow

    val listener = View.OnApplyWindowInsetsListener { _, insets ->

        trySend(insets)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsets.CONSUMED
        } else {
            insets
        }
    }

    decorView.setOnApplyWindowInsetsListener(listener)

    decorView.rootWindowInsets?.let {

        trySend(it)
    }

    decorView.post {

        trySend(decorView.rootWindowInsets ?: return@post)
    }

    awaitClose {

        decorView.setOnApplyWindowInsetsListener(null)
    }
}

private fun ComponentCallbacks.getActivity() = this.asObjectOrNull<Fragment>()?.activity ?: this.asObject<Activity>()
