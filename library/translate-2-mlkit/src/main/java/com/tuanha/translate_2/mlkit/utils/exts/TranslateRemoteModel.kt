package com.tuanha.translate_2.mlkit.utils.exts

import android.content.Context
import androidx.lifecycle.asFlow
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translator
import com.simple.coreapp.utils.ext.isInternetAvailable
import com.simple.coreapp.utils.extentions.resumeActive
import com.simple.crashlytics.logCrashlytics
import com.simple.state.ResultState
import com.simple.state.flatMapLatestState
import com.tuanha.translate_2.mlkit.MlkitTranslate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import java.net.UnknownHostException


internal suspend fun TranslateRemoteModel.checkModelDownloadedAsync() = channelFlow {

    RemoteModelManager.getInstance().isModelDownloaded(this@checkModelDownloadedAsync).addOnSuccessListener {

        trySend(ResultState.Success(it))
    }.addOnFailureListener {

        trySend(ResultState.Failed(it))
    }

    awaitClose {

    }
}

internal suspend fun TranslateRemoteModel.checkModelDownloaded() = channelFlow {

    RemoteModelManager.getInstance().isModelDownloaded(this@checkModelDownloaded).addOnSuccessListener {

        trySend(ResultState.Success(it))
    }.addOnFailureListener {

        trySend(ResultState.Failed(it))
    }

    awaitClose {

    }
}.first()

internal suspend fun TranslateRemoteModel.downloadModelAsync() = channelFlow {

    val application = MlkitTranslate.applicationFlow.asFlow().first()

    if (!application.isInternetAvailable()) {

        trySend(ResultState.Failed(UnknownHostException()))
        awaitClose()
        return@channelFlow
    }


    RemoteModelManager.getInstance().download(this@downloadModelAsync, DownloadConditions.Builder().build()).addOnSuccessListener {

        trySend(ResultState.Success(true))
    }.addOnFailureListener {

        trySend(ResultState.Failed(it))
    }

    awaitClose {

    }
}

internal suspend fun TranslateRemoteModel.downloadModel() = channelFlow {

    RemoteModelManager.getInstance().download(this@downloadModel, DownloadConditions.Builder().build()).addOnSuccessListener {

        trySend(ResultState.Success(true))
    }.addOnFailureListener {

        trySend(ResultState.Failed(it))
    }

    awaitClose {

    }
}.first()


internal suspend fun TranslateRemoteModel.checkAndDownloadAsync(): Flow<ResultState<Boolean>> = let {

    this.checkModelDownloadedAsync()
}.flatMapLatestState {

    if (it) {
        flowOf(ResultState.Success(it))
    } else {
        this.downloadModelAsync()
    }
}

internal suspend fun checkAndDownloadAsync(languageCode: String) = TranslateRemoteModel.Builder(languageCode).build().checkAndDownloadAsync()



//
//internal suspend fun checkDownloadModel(languageCode: String): Translate.State {
//
//    val state = TranslateRemoteModel.Builder(languageCode).build().checkModelDownloaded()
//
//    return if (state is ResultState.Success) {
//
//        if (state.data) Translate.State.READY else Translate.State.NOT_READY
//    } else {
//
//        Translate.State.NO_SUPPORT
//    }
//}

internal suspend fun translateText(translator: Translator, text: String): ResultState<String> = suspendCancellableCoroutine { continuation ->

    translator.translate(text).addOnSuccessListener { translatedText ->

        continuation.resumeActive(ResultState.Success(translatedText))
    }.addOnFailureListener {

        logCrashlytics("mlkit_translate_failed", it, "input" to text)

        continuation.resumeActive(ResultState.Failed(it))
    }
}

internal suspend fun downloadModelIfNeededTimeout(context: Context, languageCode: String): ResultState<Boolean> {

    val remoteModel = TranslateRemoteModel.Builder(languageCode).build()

    val checkModelDownloadedState = remoteModel.checkModelDownloaded()

    if (checkModelDownloadedState is ResultState.Failed) {

        throw checkModelDownloadedState.cause
    }

    if (checkModelDownloadedState is ResultState.Success && checkModelDownloadedState.data) {

        return checkModelDownloadedState
    }

    if (!context.isInternetAvailable()) {

        throw UnknownHostException()
    }

    return withTimeout(2 * 60 * 1000) {

        remoteModel.downloadModel()
    }
}
