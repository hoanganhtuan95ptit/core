package com.one.config.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.one.coreapp.data.task.config.Config
import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.utils.extentions.log
import com.one.coreapp.utils.extentions.logException
import com.one.coreapp.utils.extentions.resumeActive
import kotlinx.coroutines.suspendCancellableCoroutine

class FirebaseConfig : Config {

    override suspend fun execute(param: Config.Param): ResultState<String> = suspendCancellableCoroutine { continuation ->

        val config = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(param.timeOut)
            .build()

        config.setConfigSettingsAsync(configSettings)

        config.fetchAndActivate().addOnSuccessListener {

            log("firebase config", param.key)

            continuation.resumeActive(ResultState.Success(config.getString(param.key)))
        }.addOnFailureListener {

            logException(RuntimeException("firebase config", it))

            continuation.resumeActive(ResultState.Failed(it))
        }
    }
}