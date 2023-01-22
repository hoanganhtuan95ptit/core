package com.one.detect.data.usecase

import com.one.coreapp.data.usecase.BaseUseCase
import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.utils.extentions.executeByPriority
import com.one.detect.DetectTask
import com.one.detect.entities.TextBlock

class DetectUseCase(
    private val list: List<DetectTask>
) : BaseUseCase<DetectUseCase.Param, ResultState<List<TextBlock>>> {

    override suspend fun execute(param: Param?): ResultState<List<TextBlock>> {
        checkNotNull(param)

        return list.executeByPriority(DetectTask.Param(param.path, param.inputCode, param.outputCode))
    }

    data class Param(val path: String, val inputCode: String, val outputCode: String) : BaseUseCase.Param()
}