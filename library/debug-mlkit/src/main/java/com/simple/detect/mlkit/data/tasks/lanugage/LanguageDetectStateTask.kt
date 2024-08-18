package com.simple.detect.mlkit.data.tasks.lanugage

import com.simple.task.LowException
import com.simple.task.Task

abstract class LanguageDetectStateTask : Task<LanguageDetectStateTask.Param, Int>, LanguageSupportTask {

    override suspend fun executeTask(param: Param): Int {

        return if (!checkSupport(param.languageCode)) {
            throw LowException("")
        } else {
            1
        }
    }

    data class Param(val languageCode: String)
}