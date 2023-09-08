package com.simple.detect.data.usecase

import com.simple.coreapp.data.usecase.BaseUseCase
import com.simple.state.ResultState
import com.simple.task.executeAsyncByPriority
import com.simple.detect.DetectTask
import com.simple.detect.entities.DetectOption
import com.simple.detect.entities.Paragraph

class DetectUseCase(
    private val list: List<DetectTask>
) : BaseUseCase<DetectUseCase.Param, ResultState<List<Paragraph>>> {

    override suspend fun execute(param: Param?): ResultState<List<Paragraph>> {
        checkNotNull(param)

        return list.executeAsyncByPriority(DetectTask.Param(param.path, param.inputCode, param.outputCode, param.detectOption, param.sizeMax))
    }

    data class Param(val path: String, val inputCode: String, val outputCode: String, val detectOption: DetectOption, val sizeMax: Int) : BaseUseCase.Param()
}