package com.simple.detect_2

import android.graphics.Bitmap
import android.util.Log
import com.simple.autobind.AutoBind
import com.simple.crashlytics.logCrashlytics
import com.simple.detect_2.entities.Paragraph
import com.simple.state.ResultState
import com.simple.state.doFailed
import com.simple.state.isSuccess
import com.simple.state.map
import com.simple.state.mapToState
import kotlinx.coroutines.flow.map

interface DetectTask {

    suspend fun isSupportState(languageCode: String): ResultState<Boolean>

    suspend fun isSupport(languageCode: String): Boolean


    suspend fun detectState(source: Bitmap): ResultState<List<Paragraph>>

    suspend fun detect(source: Bitmap): List<Paragraph>


    companion object {

        val instant = AutoBind.loadAsync(DetectTask::class.java, true)

        fun isSupportState(languageCode: String) = let {

            findTask(languageCode = languageCode)
        }.map {

            it.doFailed {

                Log.d("tuanha", "isSupportState: ", it)
                logCrashlytics("isSupportState", it)
            }

            it
        }

        fun detectState(languageCode: String, source: Bitmap) = let {

            findTask(languageCode = languageCode)
        }.mapToState {

            it.detectState(source = source)
        }.map {

            it.doFailed {

                Log.d("tuanha", "detectState: ", it)
                logCrashlytics("detectState", it)
            }

            it
        }


        private fun findTask(languageCode: String) = instant.map { tasks ->

            val list = tasks.map { task ->

                task.isSupportState(languageCode = languageCode).map { task }
            }

            list.find { it.isSuccess() } ?: list.first()
        }
    }
}