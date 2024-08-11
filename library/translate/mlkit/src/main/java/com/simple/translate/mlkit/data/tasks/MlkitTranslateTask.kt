package com.simple.translate.mlkit.data.tasks

import android.content.Context
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.simple.analytics.logAnalytics
import com.simple.coreapp.utils.ext.isInternetAvailable
import com.simple.coreapp.utils.extentions.resumeActive
import com.simple.crashlytics.logCrashlytics
import com.simple.state.ResultState
import com.simple.state.isFailed
import com.simple.state.toSuccess
import com.simple.translate.data.tasks.TranslateTask
import com.simple.translate.mlkit.utils.exts.checkModelDownloaded
import com.simple.translate.mlkit.utils.exts.downloadModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

class MlkitTranslateTask(
    private val context: Context
) : TranslateTask {

    private val map: Map<String, String> = hashMapOf(
    )

    override suspend fun executeTask(param: TranslateTask.Param): List<String> = withContext(coroutineContext) {

        val inputCode = map[param.inputCode] ?: param.inputCode

        val outputCode = map[param.outputCode] ?: param.outputCode


        if (inputCode == outputCode) {

            return@withContext param.text
        }


        val listSupported = TranslateLanguage.getAllLanguages()


        // check language support
        listOf(inputCode, outputCode).find {

            inputCode.lowercase() !in listSupported
        }?.let {

            logAnalytics("mlkit_translate_task_not_support", "code" to it)
            throw RuntimeException("not support code:$it")
        }


        logAnalytics("mlkit_translate_task_run_with_language", "inputCode" to inputCode, "outputCode" to outputCode)


        val downloadInputCodeStateDeferred = async {

            downloadModelIfNeededTimeout(inputCode)
        }

        val downloadOutputCodeStateDeferred = async {

            downloadModelIfNeededTimeout(outputCode)
        }


        val downloadInputCodeState = downloadInputCodeStateDeferred.await()

        if (downloadInputCodeState is ResultState.Failed) {

            throw downloadInputCodeState.cause
        }


        val downloadOutputCodeState = downloadOutputCodeStateDeferred.await()

        if (downloadOutputCodeState is ResultState.Failed) {

            throw downloadOutputCodeState.cause
        }


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(inputCode)
            .setTargetLanguage(outputCode)
            .build()


        val translator = Translation.getClient(options)


        val translateStateList = param.text.map {

            async {
                translate(translator, it)
            }
        }.awaitAll()


        val stateError = translateStateList.find { it.isFailed() } as? ResultState.Failed

        if (stateError != null) {

            throw stateError.cause
        }

        val list = translateStateList.map {

            it.toSuccess()?.data.orEmpty()
        }

        return@withContext list
    }

    private suspend fun downloadModelIfNeededTimeout(languageCode: String): ResultState<Boolean> {

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

    private suspend fun translate(translator: Translator, text: String): ResultState<String> = suspendCancellableCoroutine { continuation ->

        translator.translate(text).addOnSuccessListener { translatedText ->

            continuation.resumeActive(ResultState.Success(translatedText))
        }.addOnFailureListener {

            logCrashlytics("mlkit_translate_failed", it, "input" to text)

            continuation.resumeActive(ResultState.Failed(it))
        }
    }
}