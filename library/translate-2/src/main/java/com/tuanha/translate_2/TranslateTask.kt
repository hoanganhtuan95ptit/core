package com.tuanha.translate_2

import com.simple.autobind.AutoBind
import com.tuanha.translate_2.entities.Translate

interface TranslateTask {

    suspend fun translate(input: List<Translate.Request>, outputLanguageCode: String): List<Translate.Response>

    companion object {

        fun instant() = AutoBind.loadNameAsync(TranslateTask::class.java, true)
    }
}