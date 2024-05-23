package com.simple.translate.mlkit.data.tasks

import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.simple.coreapp.utils.extentions.resumeActive
import com.simple.crashlytics.logCrashlytics
import com.simple.translate.data.tasks.TranslateStateTask
import kotlinx.coroutines.suspendCancellableCoroutine

class MlkitTranslateStateTask : TranslateStateTask {

    override suspend fun executeTask(param: TranslateStateTask.Param): Int {

        return if (param.languageCode !in TranslateLanguage.getAllLanguages()) {

            -1
        } else {

            checkDownloadModel(param.languageCode)
        }
    }

    private suspend fun checkDownloadModel(languageCode: String) = suspendCancellableCoroutine { continuation ->

        val remoteModel = TranslateRemoteModel.Builder(languageCode).build()

        RemoteModelManager.getInstance().isModelDownloaded(remoteModel).addOnSuccessListener {

            continuation.resumeActive(if (it) 1 else 0)
        }.addOnFailureListener {

            logCrashlytics(RuntimeException("mlkit translate task download model if needed $languageCode", it))

            continuation.resumeActive(-1)
        }
    }
}