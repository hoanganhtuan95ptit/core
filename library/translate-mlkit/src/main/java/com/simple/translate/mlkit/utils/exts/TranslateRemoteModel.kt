package com.simple.translate.mlkit.utils.exts

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.simple.state.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first


internal suspend fun TranslateRemoteModel.checkModelDownloaded() = channelFlow<ResultState<Boolean>> {

    RemoteModelManager.getInstance().isModelDownloaded(this@checkModelDownloaded).addOnSuccessListener {

        trySend(ResultState.Success(it))
    }.addOnFailureListener {

        trySend(ResultState.Failed(it))
    }

    awaitClose {

    }
}.first()

internal suspend fun TranslateRemoteModel.downloadModel() = channelFlow {

    RemoteModelManager.getInstance().download(this@downloadModel, DownloadConditions.Builder().build()).addOnSuccessListener {

        trySend(ResultState.Success(true))
    }.addOnFailureListener {

        trySend(ResultState.Failed(it))
    }

    awaitClose {

    }
}.first()

