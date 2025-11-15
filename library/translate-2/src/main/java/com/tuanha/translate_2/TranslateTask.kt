package com.tuanha.translate_2

import com.simple.autobind.AutoBind
import com.simple.state.ResultState
import com.tuanha.translate_2.entities.Translate

interface TranslateTask {

    suspend fun isSupportState(vararg languageCode: String): ResultState<Boolean>

    suspend fun isSupport(vararg languageCode: String): Boolean


    suspend fun translateState(input: List<Translate.Request>, outputLanguageCode: String): ResultState<List<Translate.Response>>

    suspend fun translate(input: List<Translate.Request>, outputLanguageCode: String): List<Translate.Response>


    companion object {

        val instant = AutoBind.loadAsync(TranslateTask::class.java, true)
    }
}