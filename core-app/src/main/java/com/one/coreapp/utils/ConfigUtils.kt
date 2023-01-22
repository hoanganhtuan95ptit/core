package com.one.coreapp.utils

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object ConfigUtils {

    suspend fun configAsync(fieldName: String = "", time: Long = 60 * 60L) = suspendCancellableCoroutine<String> { continuation ->

        val config = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(time)
            .build()

        config.setConfigSettingsAsync(configSettings)

        config.fetchAndActivate().addOnCompleteListener {

            if (!continuation.isCompleted) continuation.resume(config.getString(fieldName))
        }
    }
}