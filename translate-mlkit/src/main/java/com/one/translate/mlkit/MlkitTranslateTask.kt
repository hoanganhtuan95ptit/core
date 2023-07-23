package com.one.translate.mlkit

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.one.state.ResultState
import com.one.state.isFailed
import com.one.coreapp.utils.Lock
import com.one.analytics.logAnalytics
import com.one.crashlytics.logCrashlytics
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

        val inputCode = map[param.inputCode] ?: param.inputCode

        val outputCode = map[param.outputCode] ?: param.outputCode


        val listBreak = listOf("", "wo", "und-latn")


        if (inputCode.lowercase() in listBreak || outputCode.lowercase() in listBreak || inputCode == outputCode) {

            return@withContext ResultState.Failed(java.lang.RuntimeException("not support inputCode:$inputCode outputCode:$outputCode"))
        }


        logAnalytics("mlkit translate task step 1 inputCode:$inputCode outputCode:$outputCode", "")


        val downloadInputCodeStateDeferred = async {

            Lock.withLock(inputCode) { downloadModelIfNeededTimeout(inputCode) }
        }

        val downloadOutputCodeStateDeferred = async {

            Lock.withLock(outputCode) { downloadModelIfNeededTimeout(outputCode) }
        }


        val downloadInputCodeState = downloadInputCodeStateDeferred.await()

        if (downloadInputCodeState.isFailed()) {

            return@withContext downloadInputCodeState
        }


        val downloadOutputCodeState = downloadOutputCodeStateDeferred.await()

        if (downloadOutputCodeState.isFailed()) {

            return@withContext downloadOutputCodeState
        }


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(inputCode)
            .setTargetLanguage(outputCode)
            .build()


        val translator = Translation.getClient(options)


        logAnalytics("mlkit translate task step 2 inputCode:$inputCode outputCode:$outputCode", "")


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

        val remoteModel = TranslateRemoteModel.Builder(languageCode).build()

        modelManager.download(remoteModel, downloadConditions).addOnSuccessListener {

            continuation.resumeActive(ResultState.Success(emptyList()))
        }.addOnFailureListener {

            logCrashlytics(RuntimeException("mlkit translate task download model if needed $languageCode", it))

            continuation.resumeActive(ResultState.Failed(it))
        }
    }

    private suspend fun translate(translator: Translator, text: String): ResultState<String> = suspendCancellableCoroutine { continuation ->

        translator.translate(text).addOnSuccessListener { translatedText ->

            continuation.resumeActive(ResultState.Success(translatedText))
        }.addOnFailureListener {

            logCrashlytics(RuntimeException("mlkit translate task translate", it))

            continuation.resumeActive(ResultState.Failed(it))
        }
    }
}