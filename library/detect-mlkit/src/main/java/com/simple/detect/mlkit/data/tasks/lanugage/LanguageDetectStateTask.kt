package com.simple.detect.mlkit.data.tasks.lanugage

import com.simple.detect.entities.DetectState
import com.simple.task.LowException
import com.simple.task.Task

abstract class LanguageDetectStateTask : Task<LanguageDetectStateTask.Param, DetectState>, LanguageSupportTask {

    override suspend fun executeTask(param: Param): DetectState {

        return if (!checkSupport(param.languageCode)) {
            throw LowException("")
        } else {
            DetectState.READY
        }
    }

    override suspend fun logStart(param: Param, taskId: String) {
    }

    override suspend fun logSuccess(param: Param, taskId: String) {
    }

    override suspend fun logFailed(param: Param, taskId: String, throwable: Throwable) {
    }

    data class Param(val languageCode: String)
}