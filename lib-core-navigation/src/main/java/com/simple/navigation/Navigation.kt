package com.simple.navigation

import android.content.ComponentCallbacks
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.simple.coreapp.utils.extentions.getViewModelGlobal
import com.simple.navigation.domain.entities.NavigationEvent
import com.simple.navigation.utils.ext.findParentNavigationOrNull

interface Navigation {

    suspend fun offerNavigationDeepLink(deepLink: String): Boolean {

        val viewModel = getViewModelGlobal(NavigationViewModel::class)


        val provider = viewModel.getListProvider(deepLink) ?: return false

        val navigationEvent = provider.second.firstNotNullOfOrNull { it.provideNavigationEvent(deepLink, provider.first) } ?: return false


        return offerNavigationEvent(navigationEvent)
    }

    suspend fun offerNavigationEvent(event: NavigationEvent): Boolean {

        if (onNavigationEvent(event)) {

            return true
        }

        if (this !is ComponentCallbacks) {

            return false
        }

        return findParentNavigationOrNull()?.offerNavigationEvent(event) ?: false
    }

    suspend fun onNavigationEvent(event: NavigationEvent): Boolean {

        val fragment = event.provideFragment() ?: return false


        val listScope = event.provideListScope()


        listScope.firstOrNull {

            it.isInstance(this)
        }?.let {

            return navigateTo(event, it, fragment, tag = event.provideTag())
        }

        return if (this is FragmentActivity) {

            navigateTo(event, null, fragment, tag = event.provideTag())
        } else {

            false
        }
    }

    suspend fun navigateTo(event: NavigationEvent, scope: Class<*>?, fragment: Fragment, tag: String?): Boolean {

        val containerViewId: Int

        val fragmentManager: FragmentManager


        when (this) {

            is Fragment -> {

                containerViewId = view?.id ?: 0

                fragmentManager = childFragmentManager
            }

            is FragmentActivity -> {

                containerViewId = findViewById<ViewGroup>(android.R.id.content).children.first().id

                fragmentManager = supportFragmentManager
            }

            else -> {

                return false
            }
        }


        val screenLast = fragmentManager.fragments.lastOrNull()


        if (screenLast?.javaClass?.isInstance(fragment) == true && screenLast is ChildNavigation) {

            screenLast.updateEvent(event)
        } else {

            navigateTo(fragmentManager, containerViewId, fragment, tag)
        }


        return true
    }

    suspend fun navigateTo(fragmentManager: FragmentManager, @IdRes containerViewId: Int, fragment: Fragment, tag: String?) {

        if (fragment is DialogFragment) {

            fragment.show(fragmentManager, tag)
        } else fragmentManager.commit {

            replace(containerViewId, fragment, tag).addToBackStack("")
        }
    }
}

interface ChildNavigation {

    fun updateEvent(event: NavigationEvent) {
    }
}