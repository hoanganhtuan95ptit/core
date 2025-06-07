@file:Suppress("DEPRECATION")

package com.unknown.size.uitls.exts

import android.app.Activity
import android.content.ComponentCallbacks
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.simple.core.utils.extentions.asObject
import com.simple.core.utils.extentions.asObjectOrNull
import com.unknown.coroutines.launchCollect
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull


fun ComponentCallbacks.doOnHeightStatusAndHeightNavigationChange(onChange: suspend (heightStatusBar: Int, heightNavigationBar: Int) -> Unit) {

    val lifecycleOwner: LifecycleOwner = this.asObjectOrNull<Fragment>()?.viewLifecycleOwner ?: this.asObjectOrNull<AppCompatActivity>() ?: return

    listenerOnHeightStatusAndHeightNavigationChange().launchCollect(lifecycleOwner) {

        onChange(it.first, it.second)
    }
}


fun ComponentCallbacks.listenerOnHeightStatusAndHeightNavigationChange() = listenerOnApplyWindowInsetsAsync().mapNotNull { insets ->

    val activity = getActivity()
    val heightStatusBar = insets.getStatusBar().takeIf { it >= 10 } ?: activity.statusBarHeight()
    val heightNavigationBar = insets.getNavigationBar().takeIf { it >= 10 } ?: activity.navigationBarHeight()

    if (heightStatusBar <= 0 || heightNavigationBar <= 0) {
        null
    } else {
        Pair(heightStatusBar, heightNavigationBar)
    }
}.distinctUntilChanged()


fun ComponentCallbacks.listenerOnApplyWindowInsetsAsync() = channelFlow {

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
