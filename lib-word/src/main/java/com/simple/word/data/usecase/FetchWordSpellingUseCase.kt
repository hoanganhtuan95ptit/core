package com.simple.word.data.usecase

import com.simple.coreapp.data.usecase.BaseUseCase
import com.simple.state.ResultState
import com.simple.task.executeAsyncByFast
import com.simple.analytics.logAnalytics
import com.simple.word.data.task.spelling.SpellingTask
import com.simple.word.entities.Spelling

class FetchWordSpellingUseCase(
    private val list: List<SpellingTask>
) : BaseUseCase<FetchWordSpellingUseCase.Param, ResultState<List<Spelling>>> {

    override suspend fun execute(param: Param?): ResultState<List<Spelling>> {
        checkNotNull(param)

        logAnalytics("fetch word spelling use case ${param.inputCode}" to "")

        return list.executeAsyncByFast(SpellingTask.Param(param.text, param.inputCode))
    }

    data class Param(val text: String, val inputCode: String) : BaseUseCase.Param()
}