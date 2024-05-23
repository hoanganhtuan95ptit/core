package com.simple.translate.data.tasks

import com.simple.task.Task

interface TranslateStateTask : Task<TranslateStateTask.Param, Int> {

    data class Param(val languageCode: String)
}