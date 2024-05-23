package com.simple.translate.mlkit.data.tasks

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.simple.analytics.logAnalytics
import com.simple.coreapp.utils.extentions.resumeActive
import com.simple.crashlytics.logCrashlytics
import com.simple.state.ResultState
import com.simple.state.isFailed
import com.simple.translate.data.tasks.TranslateTask
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.coroutineContext

class MlkitTranslateTask : TranslateTask {

    private val map: Map<String, String> = hashMapOf(
    )

    override suspend fun execute(param: TranslateTask.Param): ResultState<List<String>> = withContext(coroutineContext) {

        val inputCode = map[param.inputCode] ?: param.inputCode

        val outputCode = map[param.outputCode] ?: param.outputCode


        val listSupported = TranslateLanguage.getAllLanguages()


        if (inputCode.lowercase() !in listSupported || outputCode.lowercase() !in listSupported || inputCode == outputCode) {

            return@withContext ResultState.Failed(java.lang.RuntimeException("not support inputCode:$inputCode outputCode:$outputCode"))
        }


        logAnalytics("Mlkit_Translate_Task_InputCode $inputCode" to inputCode)
        logAnalytics("Mlkit_Translate_Task_OutputCode $outputCode" to outputCode)


        val downloadInputCodeStateDeferred = async {

            downloadModelIfNeededTimeout(inputCode)
        }

        val downloadOutputCodeStateDeferred = async {

            downloadModelIfNeededTimeout(outputCode)
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


        val translateStateList = param.text.map {

            async {
                translate(translator, it)
            }
        }.awaitAll()


        val stateError = translateStateList.find { it.isFailed() } as? ResultState.Failed


        if (stateError != null) {

            return@withContext ResultState.Failed(stateError.cause)
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

        RemoteModelManager.getInstance().download(remoteModel, DownloadConditions.Builder().build()).addOnSuccessListener {

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