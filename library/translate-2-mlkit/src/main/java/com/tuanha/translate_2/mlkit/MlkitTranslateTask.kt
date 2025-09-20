package com.tuanha.translate_2.mlkit

import android.content.Context
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.simple.analytics.logAnalytics
import com.simple.autobind.annotation.AutoBind
import com.simple.startapp.StartApp
import com.simple.state.ResultState
import com.tuanha.translate_2.TranslateTask
import com.tuanha.translate_2.entities.Translate
import com.tuanha.translate_2.mlkit.utils.exts.downloadModelIfNeededTimeout
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

@AutoBind(TranslateTask::class)
class MlkitTranslateTask() : TranslateTask {

    override suspend fun translate(input: List<Translate.Request>, outputLanguageCode: String): List<Translate.Response> = withContext(coroutineContext) {


        val context = StartApp.applicationFlow.mapNotNull { it }.first()


        val list = input.groupBy {

            it.languageCode
        }.map {

            async {
                translateTextAndException(context = context, texts = it.value.map { it.text }, inputLanguageCode = it.key, outputLanguageCode = outputLanguageCode)
            }
        }.awaitAll()


        return@withContext list.flatMap { it }
    }

    private suspend fun translateTextAndException(context: Context, texts: List<String>, inputLanguageCode: String, outputLanguageCode: String): List<Translate.Response> = runCatching {

        translateText(context = context, texts = texts, inputLanguageCode = inputLanguageCode, outputLanguageCode = outputLanguageCode)
    }.getOrElse { e ->

        texts.map { Translate.Response(text = it, state = ResultState.Failed(cause = e)) }
    }

    private suspend fun translateText(context: Context, texts: List<String>, inputLanguageCode: String, outputLanguageCode: String): List<Translate.Response> = withContext(coroutineContext) {

        val inputCode = wrapLanguageCode(inputLanguageCode)

        val outputCode = wrapLanguageCode(outputLanguageCode)


        if (inputCode == outputCode) return@withContext texts.map {

            Translate.Response(text = it, state = ResultState.Success(it))
        }


        // kiểm tra xem có hỗ trợ ngôn ngữ không
        val listSupported = TranslateLanguage.getAllLanguages()

        listOf(inputCode, outputCode).find {

            inputCode.lowercase() !in listSupported
        }?.let {

            logAnalytics("mlkit_translate_task_not_support", "code" to it)
            throw RuntimeException("not support code:$it")
        }


        logAnalytics("mlkit_translate_task_run_with_language", "inputCode" to inputCode, "outputCode" to outputCode)


        // tải bản dịch
        val downloadInputCodeStateDeferred = async {

            downloadModelIfNeededTimeout(context = context, languageCode = inputCode)
        }

        val downloadOutputCodeStateDeferred = async {

            downloadModelIfNeededTimeout(context = context, languageCode = outputCode)
        }


        val downloadInputCodeState = downloadInputCodeStateDeferred.await()

        if (downloadInputCodeState is ResultState.Failed) {

            throw downloadInputCodeState.cause
        }


        val downloadOutputCodeState = downloadOutputCodeStateDeferred.await()

        if (downloadOutputCodeState is ResultState.Failed) {

            throw downloadOutputCodeState.cause
        }


        // thực hiện dịch
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(inputCode)
            .setTargetLanguage(outputCode)
            .build()


        val translator = Translation.getClient(options)


        return@withContext texts.map {

            Translate.Response(text = it, state = com.tuanha.translate_2.mlkit.utils.exts.translateText(translator = translator, text = it))
        }
    }

    private fun wrapLanguageCode(languageCode: String): String {
        return languageCode
    }
}