package com.simple.startapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.simple.autobind.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

@SuppressLint("StaticFieldLeak")
object StartApp {

    val applicationFlow = MutableStateFlow<Application?>(null)

    val activityResumeFlow = MutableStateFlow<Activity?>(null)

    fun isInstalled(moduleName: String) = channelFlow {

        val application = applicationFlow.mapNotNull { it }.first()

        val splitInstallManager = SplitInstallManagerFactory.create(application)

        if (splitInstallManager.installedModules.contains(moduleName)) {

            trySend(1)
        } else {

            trySend(0)
        }

        awaitClose {

        }
    }

    fun downloadModuleAsync(moduleName: String) = channelFlow {

        val application = applicationFlow.mapNotNull { it }.first()

        val splitInstallManager = SplitInstallManagerFactory.create(application)

        if (splitInstallManager.installedModules.contains(moduleName)) {

            trySend(ResultState.Success(SplitInstallSessionStatus.INSTALLED))
            awaitClose { }
        }

        val request = SplitInstallRequest
            .newBuilder()
            .addModule(moduleName) // tên module trong settings.gradle
            .build()

        val splitInstallStateUpdatedListener = object : SplitInstallStateUpdatedListener {

            override fun onStateUpdate(state: SplitInstallSessionState) {
                trySend(ResultState.Success(state.status()))
            }
        }

        splitInstallManager.registerListener(splitInstallStateUpdatedListener)

        splitInstallManager.startInstall(request).addOnFailureListener { exception ->

            trySend(ResultState.Failed(exception))
        }

        awaitClose {

            splitInstallManager.unregisterListener(splitInstallStateUpdatedListener)
        }
    }

    fun deleteAll() = channelFlow {

        val application = applicationFlow.mapNotNull { it }.first()

        val splitInstallManager = SplitInstallManagerFactory.create(application)

        splitInstallManager.deferredUninstall(splitInstallManager.installedModules.toList()).addOnSuccessListener {
            trySend(ResultState.Success(1))
        }.addOnFailureListener {
            trySend(ResultState.Failed(it))
        }

        awaitClose {

        }
    }

    fun deleteModule(vararg moduleName: String) = channelFlow {

        val application = applicationFlow.mapNotNull { it }.first()

        val splitInstallManager = SplitInstallManagerFactory.create(application)

        splitInstallManager.deferredUninstall(moduleName.toList()).addOnSuccessListener {
            trySend(1)
        }.addOnFailureListener {
            trySend(0)
        }

        awaitClose {

        }
    }
}