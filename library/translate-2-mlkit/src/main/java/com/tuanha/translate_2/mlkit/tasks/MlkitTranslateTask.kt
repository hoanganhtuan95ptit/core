package com.tuanha.translate_2.mlkit.tasks

import android.content.Context
import androidx.lifecycle.asFlow
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.simple.analytics.logAnalytics
import com.simple.autobind.annotation.AutoBind
import com.simple.crashlytics.logCrashlytics
import com.simple.state.ResultState
import com.simple.state.doFailed
import com.simple.state.flatMapLatestState
import com.simple.state.isFailed
import com.simple.state.isSuccess
import com.tuanha.translate_2.TranslateTask
import com.tuanha.translate_2.entities.Translate
import com.tuanha.translate_2.mlkit.MlkitTranslate
import com.tuanha.translate_2.mlkit.utils.exts.checkAndDownloadAsync
import com.tuanha.translate_2.mlkit.utils.exts.downloadModelIfNeededTimeout
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

@AutoBind(TranslateTask::class)
class MlkitTranslateTask() : TranslateTask {

    override suspend fun isSupportState(vararg languageCode: String): ResultState<Boolean> = let {

        val listWrap = languageCode.map {

            wrapLanguageCode(it).lowercase()
        }

        // kiểm tra xem có hỗ trợ ngôn ngữ không
        val listSupported = TranslateLanguage.getAllLanguages()


        val languageNotSupport = listWrap.find {

            it !in listSupported
        }

        val state = if (languageNotSupport != null) {

            ResultState.Failed(RuntimeException("not support language:$languageNotSupport"))
        } else {

            ResultState.Success(listWrap)
        }

        flowOf(state)
    }.flatMapLatestState { listWrap ->

        val flows = listWrap.map {
            checkAndDownloadAsync(it)
        }

        combine(flows = flows) { list ->

            list.find { it.isFailed() } ?: ResultState.Success(true)
        }
    }.map {

        it.doFailed {

            logCrashlytics("isSupportState", it)
        }

        it
    }.first()

    override suspend fun isSupport(vararg languageCode: String): Boolean = isSupportState(languageCode = languageCode).isSuccess()


    override suspend fun translateState(input: List<Translate.Request>, outputLanguageCode: String): ResultState<List<Translate.Response>> {

        TODO("Not yet implemented")
    }

    override suspend fun translate(input: List<Translate.Request>, outputLanguageCode: String): List<Translate.Response> = withContext(coroutineContext) {


        val application = MlkitTranslate.applicationFlow.asFlow().first()


        val list = input.groupBy {

            it.languageCode
        }.map {

            async {
                translateTextAndException(context = application, texts = it.value.map { it.text }, inputLanguageCode = it.key, outputLanguageCode = outputLanguageCode)
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