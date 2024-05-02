package com.simple.coreapp.utils.extentions

import android.app.Activity
import android.content.ComponentCallbacks
import android.os.Build
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.simple.job.JobQueueManager
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun ComponentCallbacks.doOnHeightStatusAndHeightNavigationChange(onChange: (heightStatusBar: Int, heightNavigationBar: Int) -> Unit) {

    var _heightStatusBar = 0
    var _heightNavigationBar = 0

    doOnApplyWindowInsets {

        val heightStatusBar = getHeightStatusBarOrNull(it).apply {

        } ?: STATUS_BAR_DEFAULT

        val heightNavigationBar = getHeightNavigationBarOrNull(it).apply {

        } ?: NAVIGATION_BAR_DEFAULT


        if (_heightStatusBar == heightStatusBar && _heightNavigationBar == heightNavigationBar) return@doOnApplyWindowInsets


        _heightStatusBar = heightStatusBar
        _heightNavigationBar = heightNavigationBar


        onChange(heightStatusBar, heightNavigationBar)
    }
}

fun ComponentCallbacks.doOnHeightNavigationChange(onChange: (heightNavigationBar: Int) -> Unit) {

    var _heightNavigationBar = 0

    doOnApplyWindowInsets {

        val heightNavigationBar = getHeightNavigationBarOrNull(it) ?: NAVIGATION_BAR_DEFAULT

        if (_heightNavigationBar == heightNavigationBar) return@doOnApplyWindowInsets
        _heightNavigationBar = heightNavigationBar

        onChange(heightNavigationBar)
    }
}

fun ComponentCallbacks.doOnHeightStatusChange(onChange: (heightStatusBar: Int) -> Unit) {

    var _heightStatusBar = 0

    doOnApplyWindowInsets {

        val heightStatusBar = getHeightStatusBarOrNull(it) ?: STATUS_BAR_DEFAULT

        if (_heightStatusBar == heightStatusBar) return@doOnApplyWindowInsets
        _heightStatusBar = heightStatusBar

        onChange(heightStatusBar)
    }
}

internal fun ComponentCallbacks.doOnApplyWindowInsets(onChange: (WindowInsets) -> Unit) {

    val activity = (this as? Activity) ?: (this as? Fragment)?.activity ?: let {

        return
    }

    val decorView = activity.window?.decorView ?: activity.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0) ?: (this as? Fragment)?.view ?: let {

        return
    }

    decorView.setOnApplyWindowInsetsListener { _, windowInsets ->

        onChange(windowInsets)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsets.CONSUMED
        } else {
            windowInsets
        }
    }

    decorView.post {

        onChange(decorView.rootWindowInsets ?: return@post)
    }

    decorView.rootWindowInsets?.let {

        onChange(it)
    }
}

fun ComponentCallbacks.getHeightStatusBarOrNull(windowInsets: WindowInsets? = null): Int? {

    windowInsets?.getHeightStatusBarOrNull()?.let {

        return it
    }

    val activity = (this as? AppCompatActivity) ?: (this as? Fragment)?.activity ?: let {

        return null
    }

    return activity.window?.decorView?.rootWindowInsets?.getHeightStatusBarOrNull().apply {


    } ?: getStatusBarHeight(activity).takeIf { it > 0 }.apply {


    }
}

fun ComponentCallbacks.getHeightNavigationBarOrNull(windowInsets: WindowInsets? = null): Int? {

    windowInsets?.getHeightNavigationBarOrNull()?.let {

        return it
    }

    val activity = (this as? AppCompatActivity) ?: (this as? Fragment)?.activity ?: let {

        return null
    }

    return activity.window?.decorView?.rootWindowInsets?.getHeightNavigationBarOrNull().apply {

    } ?: getNavigationBarHeight(activity).takeIf { it > 0 }.apply {

    }
}

fun ComponentCallbacks.queueUi(tag: String = "ui", context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {

    val coroutineContext = when (this) {
        is Fragment -> {
            context + viewLifecycleOwner.lifecycleScope.coroutineContext
        }

        is FragmentActivity -> {
            context + lifecycleScope.coroutineContext
        }

        else -> {
            context
        }
    }

    JobQueueManager.submit(tag, coroutineContext, block)
}