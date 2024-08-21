package com.simple.detect.data.tasks

import com.simple.detect.entities.DetectProvider
import com.simple.detect.entities.DetectState
import com.simple.task.Task

interface DetectStateTask : Task<DetectStateTask.Param, Pair<DetectProvider, DetectState>> {

    override suspend fun logStart(param: Param, taskId: String) {
    }

    override suspend fun logSuccess(param: Param, taskId: String) {
    }

    override suspend fun logFailed(param: Param, taskId: String, throwable: Throwable) {
    }

    data class Param(val languageCode: String)
}