package com.one.config.firebase

import com.four.config.Config
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.one.state.ResultState
import com.one.analytics.logAnalytics
import com.one.crashlytics.logCrashlytics
import com.one.coreapp.utils.extentions.resumeActive
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseConfig : Config {

    override suspend fun execute(param: Config.Param): ResultState<String> = suspendCancellableCoroutine { continuation ->

        val config = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(param.timeout)
            .build()

        config.setConfigSettingsAsync(configSettings)

        config.fetchAndActivate().addOnSuccessListener {

            logAnalytics("firebase config", param.key)

            continuation.resumeActive(ResultState.Success(config.getString(param.key)))
        }.addOnFailureListener {

            logCrashlytics(RuntimeException("firebase config", it))

            continuation.resumeActive(ResultState.Failed(it))
        }
    }
}