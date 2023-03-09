package com.one.translate

import com.one.task.Task

interface TranslateTask : Task<TranslateTask.Param, List<String>> {

    data class Param(val text: List<String>, val inputCode: String, val outputCode: String)
}