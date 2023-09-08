package com.simple.word.data.task.dictionary

import com.simple.task.Task
import com.simple.word.entities.TextLevel

interface DictionaryTask : Task<DictionaryTask.Param, List<TextLevel>> {

    data class Param(val text: String, val inputCode: String, val outputCode: String)
}