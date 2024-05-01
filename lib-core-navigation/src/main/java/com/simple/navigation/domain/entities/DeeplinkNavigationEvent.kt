package com.simple.navigation.domain.entities

import androidx.fragment.app.Fragment

data class DeeplinkNavigationEvent(val deepLink: String, val params: Map<String, String>, val tag: String? = null, val listScope: List<Class<*>> = emptyList(), val fragment: Fragment) : NavigationEvent() {

    override fun provideTag(): String? {
        return tag
    }

    override fun provideListScope(): List<Class<*>> {
        return listScope
    }

    override fun provideFragment(): Fragment {
        return fragment
    }
}