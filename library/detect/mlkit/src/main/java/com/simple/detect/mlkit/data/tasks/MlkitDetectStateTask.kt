package com.simple.detect.mlkit.data.tasks

import com.simple.detect.data.tasks.DetectStateTask
import com.simple.detect.entities.DetectProvider
import com.simple.detect.mlkit.data.tasks.lanugage.LanguageDetectStateTask
import com.simple.state.toSuccess
import com.simple.task.LowException
import com.simple.task.executeAsyncByFast

class MlkitDetectStateTask(
    val list: List<LanguageDetectStateTask>
) : DetectStateTask {

    override suspend fun executeTask(param: DetectStateTask.Param): Pair<String, Int> {

        val provider = DetectProvider.OFFLINE.value

        val state = list.executeAsyncByFast(LanguageDetectStateTask.Param(param.languageCode)).toSuccess()?.data ?: throw LowException("")

        return provider to state
    }
}