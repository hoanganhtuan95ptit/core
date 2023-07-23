package com.one.translate.data.usecase

import com.one.coreapp.data.usecase.BaseUseCase
import com.one.state.ResultState
import com.one.task.executeAsyncByPriority
import com.one.translate.TranslateTask

class TranslateUseCase(
    private val list: List<TranslateTask>
) : BaseUseCase<TranslateUseCase.Param, ResultState<List<String>>> {

    override suspend fun execute(param: Param?): ResultState<List<String>> {
        checkNotNull(param)

        return list.executeAsyncByPriority(TranslateTask.Param(param.text, param.inputCode, param.outputCode))
    }

    data class Param(val text: List<String>, val inputCode: String, val outputCode: String) : BaseUseCase.Param()
}