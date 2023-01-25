package com.one.translate.mlkit

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.data.usecase.isFailed
import com.one.coreapp.utils.extentions.log
import com.one.coreapp.utils.extentions.logException
import com.one.coreapp.utils.extentions.resumeActive
import com.one.translate.TranslateTask
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class MlkitTranslateTask : TranslateTask {

    private val modelManager = RemoteModelManager.getInstance()

    private val downloadConditions = DownloadConditions.Builder().requireWifi().build()

    private val map: Map<String, String> = hashMapOf(
    )

    override suspend fun execute(param: TranslateTask.Param): ResultState<List<String>> = withContext(coroutineContext) {

        val sourceLanguage = map[param.inputCode] ?: param.inputCode

        val targetLanguage = map[param.outputCode] ?: param.outputCode


        if (sourceLanguage.lowercase() in listOf("", "wo") || targetLanguage.lowercase() in listOf("", "wo")) {

            return@withContext ResultState.Failed(java.lang.RuntimeException("not support sourceLanguage:$sourceLanguage targetLanguage:$targetLanguage"))
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()


        val translator = Translation.getClient(options)


        val downloadState1Deferred = async {

            downloadModelIfNeededTimeout(sourceLanguage)
        }

        val downloadState2Deferred = async {

            downloadModelIfNeededTimeout(targetLanguage)
        }


        val downloadState1 = downloadState1Deferred.await()

        if (downloadState1.isFailed()) {

            return@withContext downloadState1
        }


        val downloadState2 = downloadState2Deferred.await()

        if (downloadState2.isFailed()) {

            return@withContext downloadState2
        }


        log("Mlkit Translate Task", "$sourceLanguage $targetLanguage")

        val translateStateList = param.text.map {

            async {
                translate(translator, it)
            }
        }.awaitAll()


        val stateError = translateStateList.find { it.isFailed() } as? ResultState.Failed


        if (stateError != null) {

            return@withContext ResultState.Failed(stateError.error)
        }

        return@withContext translateStateList.map {

            (it as ResultState.Success).data
        }.let {

            ResultState.Success(it)
        }
    }

    private suspend fun downloadModelIfNeededTimeout(languageCode: String) = kotlin.runCatching {

        withTimeout(2 * 60 * 1000) {
            downloadModelIfNeeded(languageCode)
        }
    }.getOrElse {

        ResultState.Failed(it)
    }

    private suspend fun downloadModelIfNeeded(languageCode: String) = suspendCancellableCoroutine<ResultState<List<String>>> { continuation ->

        modelManager.download(TranslateRemoteModel.Builder(languageCode).build(), downloadConditions).addOnSuccessListener {

            continuation.resumeActive(ResultState.Success(emptyList()))
        }.addOnFailureListener {

            logException(RuntimeException("Mlkit Translate Task download Model If Needed", it))

            continuation.resumeActive(ResultState.Failed(it))
        }
    }

    private suspend fun translate(translator: Translator, text: String): ResultState<String> = suspendCancellableCoroutine { continuation ->

        translator.translate(text).addOnSuccessListener { translatedText ->

            continuation.resumeActive(ResultState.Success(translatedText))
        }.addOnFailureListener {

            logException(RuntimeException("Mlkit Translate Task translate", it))

            continuation.resumeActive(ResultState.Failed(it))
        }
    }
}