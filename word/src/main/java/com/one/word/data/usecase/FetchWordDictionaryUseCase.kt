package com.one.word.data.usecase

import com.one.coreapp.data.usecase.BaseUseCase
import com.one.coreapp.data.usecase.ResultState
import com.one.task.executeAsyncByFast
import com.one.coreapp.utils.extentions.log
import com.one.word.data.task.dictionary.DictionaryTask
import com.one.word.entities.TextLevel

class FetchWordDictionaryUseCase(
    private val list: List<DictionaryTask>
) : BaseUseCase<FetchWordDictionaryUseCase.Param, ResultState<List<TextLevel>>> {

    override suspend fun execute(param: Param?): ResultState<List<TextLevel>> {
        checkNotNull(param)

        log("fetch word dictionary use case ${param.inputCode} ${param.outputCode}")

        return list.executeAsyncByFast(DictionaryTask.Param(param.text, param.inputCode, param.outputCode))
    }

    data class Param(val text: String, val inputCode: String, val outputCode: String) : BaseUseCase.Param()
}