package com.simple.translate.data.tasks

import com.simple.task.Task
import com.simple.translate.entities.TranslateRequest
import com.simple.translate.entities.TranslateResponse

interface TranslateTask : Task<TranslateTask.Param, List<TranslateResponse>> {

    data class Param(val input: List<TranslateRequest>, val outputCode: String)
}