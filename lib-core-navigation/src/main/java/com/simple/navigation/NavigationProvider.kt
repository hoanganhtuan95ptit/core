package com.simple.navigation

import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import com.simple.navigation.domain.entities.DeeplinkNavigationEvent
import com.simple.navigation.domain.entities.NavigationEvent

interface NavigationProvider {

    @AnyThread
    fun deepLink(): String

    @MainThread
    fun provideFragment(deepLink: String, params: Map<String, String>): Fragment


    @MainThread
    open fun provideTag(deepLink: String): String? {

        return null
    }

    @MainThread
    open fun provideScope(deepLink: String): Class<*>? {

        return null
    }

    @MainThread
    open fun provideListScope(deepLink: String): List<Class<*>> {

        return provideScope(deepLink)?.let { listOf(it) } ?: emptyList()
    }

    @MainThread
    fun provideNavigationEvent(deepLink: String, params: Map<String, String>): NavigationEvent? {

        return DeeplinkNavigationEvent(deepLink, params, provideTag(deepLink), provideListScope(deepLink), provideFragment(deepLink, params))
    }
}
