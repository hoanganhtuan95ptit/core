package com.simple.detect.data.usecase

import com.simple.detect.data.tasks.DetectTask
import com.simple.detect.entities.DetectOption
import com.simple.detect.entities.Paragraph
import com.simple.state.ResultState
import com.simple.task.executeAsyncByPriority

class DetectUseCase(
    private val list: List<DetectTask>
) {

    suspend fun execute(param: Param): ResultState<List<Paragraph>> {
        checkNotNull(param)

        return list.executeAsyncByPriority(DetectTask.Param(param.path, param.inputCode, param.outputCode, param.detectOption, param.sizeMax))
    }

    data class Param(val path: String, val inputCode: String, val outputCode: String, val detectOption: DetectOption, val sizeMax: Int)
}