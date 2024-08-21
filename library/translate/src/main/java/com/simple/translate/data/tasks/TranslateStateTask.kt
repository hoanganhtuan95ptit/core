package com.simple.translate.data.tasks

import com.simple.task.Task
import com.simple.translate.entities.TranslateProvider
import com.simple.translate.entities.TranslateState

interface TranslateStateTask : Task<TranslateStateTask.Param, Pair<TranslateProvider, TranslateState>> {

    override suspend fun logStart(param: Param, taskId: String) {
    }

    override suspend fun logSuccess(param: Param, taskId: String) {
    }

    override suspend fun logFailed(param: Param, taskId: String, throwable: Throwable) {
    }

    data class Param(val languageCode: String)
}