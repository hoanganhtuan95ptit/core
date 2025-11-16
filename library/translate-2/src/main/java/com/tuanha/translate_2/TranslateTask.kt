package com.tuanha.translate_2

import android.util.Log
import com.simple.autobind.AutoBind
import com.simple.crashlytics.logCrashlytics
import com.simple.state.ResultState
import com.simple.state.doFailed
import com.simple.state.isSuccess
import com.simple.state.map
import com.simple.state.mapToState
import com.tuanha.translate_2.entities.Translate
import kotlinx.coroutines.flow.map

interface TranslateTask {

    suspend fun isSupportState(vararg languageCode: String): ResultState<Boolean>

    suspend fun isSupport(vararg languageCode: String): Boolean


    suspend fun translateState(input: List<Translate.Request>, outputLanguageCode: String): ResultState<List<Translate.Response>>

    suspend fun translate(input: List<Translate.Request>, outputLanguageCode: String): List<Translate.Response>


    companion object {

        val instant = AutoBind.loadAsync(TranslateTask::class.java, true)

        fun isSupportState(vararg languageCode: String) = let {

            findTask(languageCode = languageCode)
        }.map {

            it.doFailed {

                Log.d("tuanha", "isSupportState: ", it)
                logCrashlytics("isSupportState", it)
            }

            it
        }

        fun translateState(input: List<Translate.Request>, outputLanguageCode: String) = let {

            findTask(outputLanguageCode, *input.groupBy { it.languageCode }.keys.toTypedArray())
        }.mapToState {

            it.translateState(input = input, outputLanguageCode = outputLanguageCode)
        }.map {

            it.doFailed {

                Log.d("tuanha", "translateState: ", it)
                logCrashlytics("translateState", it)
            }

            it
        }


        private fun findTask(vararg languageCode: String) = instant.map { tasks ->

            val list = tasks.map { task ->

                task.isSupportState(languageCode = languageCode).map { task }
            }

            list.find { it.isSuccess() } ?: list.first()
        }
    }
}