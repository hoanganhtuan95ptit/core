package com.simple.translate.data.usecase

import com.simple.coreapp.data.usecase.BaseUseCase
import com.simple.state.ResultState
import com.simple.task.executeAsyncByPriority
import com.simple.translate.TranslateTask

class TranslateUseCase(
    private val list: List<TranslateTask>
) : BaseUseCase<TranslateUseCase.Param, ResultState<List<String>>> {

    override suspend fun execute(param: Param?): ResultState<List<String>> {
        checkNotNull(param)

        return list.executeAsyncByPriority(TranslateTask.Param(param.text, param.inputCode, param.outputCode))
    }

    data class Param(val text: List<String>, val inputCode: String, val outputCode: String) : BaseUseCase.Param()
}