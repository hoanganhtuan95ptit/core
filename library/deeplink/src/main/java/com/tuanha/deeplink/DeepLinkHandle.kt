package com.tuanha.deeplink

import android.os.Bundle
import androidx.activity.ComponentActivity

interface DeepLinkHandle {

    fun getDeeplink(): String = ""

    fun acceptDeeplink(deepLink: String): Boolean {

        return deepLink == getDeeplink()
    }

    suspend fun navigation(activity: ComponentActivity, deepLink: String, extras: Bundle?): Boolean
}