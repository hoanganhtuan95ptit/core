package com.simple.detect.mlkit.data.tasks

import com.simple.detect.data.tasks.DetectStateTask
import com.simple.detect.mlkit.data.tasks.lanugage.LanguageDetectStateTask
import com.simple.state.toSuccess
import com.simple.task.executeAsyncByFast

class MlkitDetectStateTask(val list: List<LanguageDetectStateTask>) : DetectStateTask {

    override suspend fun executeTask(param: DetectStateTask.Param): Int {

        return list.executeAsyncByFast(LanguageDetectStateTask.Param(param.languageCode)).toSuccess()?.data ?: -1
    }
}