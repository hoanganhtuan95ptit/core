package com.simple.config.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.simple.analytics.logAnalytics
import com.simple.config.Config
import com.simple.core.utils.extentions.resumeActive
import com.simple.crashlytics.logCrashlytics
import com.simple.state.ResultState
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseConfig : Config {

    override suspend fun execute(param: Config.Param): ResultState<String> = suspendCancellableCoroutine { continuation ->

        val config = Firebase.remoteConfig

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(param.timeout)
            .build()

        config.setConfigSettingsAsync(configSettings)

        config.fetchAndActivate().addOnSuccessListener {

            logAnalytics("firebase config", "data" to param.key)

            continuation.resumeActive(ResultState.Success(config.getString(param.key)))
        }.addOnFailureListener {

            logCrashlytics("firebase config", it)

            continuation.resumeActive(ResultState.Failed(it))
        }
    }
}