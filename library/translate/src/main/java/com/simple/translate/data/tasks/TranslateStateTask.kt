package com.simple.translate.data.tasks

import com.simple.task.Task
import com.simple.translate.entities.TranslateProvider
import com.simple.translate.entities.TranslateState

interface TranslateStateTask : Task<TranslateStateTask.Param, Pair<TranslateProvider, TranslateState>> {

    data class Param(val languageCode: String)
}