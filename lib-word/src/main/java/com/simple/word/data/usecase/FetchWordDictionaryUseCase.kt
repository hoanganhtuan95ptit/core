package com.simple.word.data.usecase

import com.simple.coreapp.data.usecase.BaseUseCase
import com.simple.state.ResultState
import com.simple.task.executeAsyncByFast
import com.simple.analytics.logAnalytics
import com.simple.word.data.task.dictionary.DictionaryTask
import com.simple.word.entities.TextLevel

class FetchWordDictionaryUseCase(
    private val list: List<DictionaryTask>
) : BaseUseCase<FetchWordDictionaryUseCase.Param, ResultState<List<TextLevel>>> {

    override suspend fun execute(param: Param?): ResultState<List<TextLevel>> {
        checkNotNull(param)

        logAnalytics("fetch word dictionary use case ${param.inputCode} ${param.outputCode}" to "")

        return list.executeAsyncByFast(DictionaryTask.Param(param.text, param.inputCode, param.outputCode))
    }

    data class Param(val text: String, val inputCode: String, val outputCode: String) : BaseUseCase.Param()
}