package com.one.detect.data.usecase

import com.one.coreapp.data.usecase.BaseUseCase
import com.one.coreapp.data.usecase.ResultState
import com.one.task.executeAsyncByPriority
import com.one.detect.DetectTask
import com.one.detect.entities.DetectOption
import com.one.detect.entities.Paragraph

class DetectUseCase(
    private val list: List<DetectTask>
) : BaseUseCase<DetectUseCase.Param, ResultState<List<Paragraph>>> {

    override suspend fun execute(param: Param?): ResultState<List<Paragraph>> {
        checkNotNull(param)

        return list.executeAsyncByPriority(DetectTask.Param(param.path, param.inputCode, param.outputCode, param.detectOption, param.sizeMax))
    }

    data class Param(val path: String, val inputCode: String, val outputCode: String, val detectOption: DetectOption, val sizeMax: Int) : BaseUseCase.Param()
}