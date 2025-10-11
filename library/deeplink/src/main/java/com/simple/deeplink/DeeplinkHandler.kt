package com.simple.deeplink

import android.content.ComponentCallbacks
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface DeeplinkHandler {

    fun getDeeplink(): String = ""

    suspend fun acceptDeeplink(deepLink: String): Boolean = deepLink == getDeeplink()

    suspend fun acceptDeeplink(componentCallbacks: ComponentCallbacks, deepLink: String): Boolean = acceptDeeplink(deepLink)

    suspend fun navigation(componentCallbacks: ComponentCallbacks, deepLink: String, extras: Map<String, Any?>?, sharedElement: Map<String, View>?): Boolean {

        return if (componentCallbacks is Fragment) {
            navigation(componentCallbacks, deepLink, extras, sharedElement)
        } else if (componentCallbacks is AppCompatActivity) {
            navigation(componentCallbacks, deepLink, extras, sharedElement)
        } else {
            false
        }
    }

    suspend fun navigation(fragment: Fragment, deepLink: String, extras: Map<String, Any?>?, sharedElement: Map<String, View>?): Boolean {

        return false
    }

    suspend fun navigation(activity: AppCompatActivity, deepLink: String, extras: Map<String, Any?>?, sharedElement: Map<String, View>?): Boolean {

        return false
    }
}

