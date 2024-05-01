package com.simple.navigation.utils.ext

import android.app.Activity
import android.content.ComponentCallbacks
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.simple.core.utils.extentions.asObjectOrNull
import com.simple.coreapp.utils.extentions.getViewModelGlobal
import com.simple.navigation.Navigation
import com.simple.navigation.NavigationViewModel
import com.simple.navigation.domain.entities.NavigationEvent
import kotlinx.coroutines.launch

fun ComponentCallbacks.setNavigationResultListener(requestKey: String, listener: FragmentResultListener) {

    navigationViewModel().resultEvent.observe(owner()) {

        if (it.peekContent().first != requestKey) return@observe

        val data = it.getContentIfNotHandled() ?: return@observe

        listener.onFragmentResult(data.first, data.second)
    }
}

fun ComponentCallbacks.setNavigationResult(requestKey: String, result: Bundle) {

    navigationViewModel().setFragmentResult(requestKey, result)
}


fun ComponentCallbacks.offerEvent(event: NavigationEvent) {

    val navigation = findNavigationOrNull() ?: return

    globalOwner().lifecycleScope.launch {

        navigation.offerNavigationEvent(event)
    }
}

fun ComponentCallbacks.offerDeepLink(deepLink: String) {

    val navigation = findNavigationOrNull() ?: return

    globalOwner().lifecycleScope.launch {

        navigation.offerNavigationDeepLink(deepLink)
    }
}


internal fun ComponentCallbacks.findNavigationOrNull(): Navigation? {

    return (this as? Navigation) ?: findParentNavigationOrNull()
}

internal fun ComponentCallbacks.findParentNavigationOrNull(): Navigation? {

    if (this is Activity && this !is Navigation) {

        return null
    }

    if (this !is Fragment) {

        return null
    }


    var fragment: Fragment? = parentFragment

    while (fragment != null && fragment !is Navigation) {

        fragment = fragment.parentFragment
    }


    return if (fragment is Navigation) {

        fragment
    } else {

        this.activity as? Navigation
    }
}


private fun ComponentCallbacks.owner(): LifecycleOwner {

    return (this as? Fragment)?.viewLifecycleOwner ?: (this as? FragmentActivity) ?: (this as? LifecycleOwner) ?: error("not found owner")
}

private fun ComponentCallbacks.globalOwner(): LifecycleOwner {

    return this.asObjectOrNull<Fragment>()?.activity ?: (this as? FragmentActivity) ?: (this as? LifecycleOwner) ?: error("not found owner")
}

private fun ComponentCallbacks.navigationViewModel(): NavigationViewModel {

    return getViewModelGlobal(NavigationViewModel::class)
}