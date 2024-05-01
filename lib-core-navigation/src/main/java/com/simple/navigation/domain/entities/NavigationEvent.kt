package com.simple.navigation.domain.entities

import androidx.fragment.app.Fragment
import java.io.Serializable

open class NavigationEvent : Serializable {

    open fun provideTag(): String? {
        return null
    }

    open fun provideListScope(): List<Class<*>> {
        return emptyList()
    }

    open fun provideFragment(): Fragment? {
        return null
    }
}