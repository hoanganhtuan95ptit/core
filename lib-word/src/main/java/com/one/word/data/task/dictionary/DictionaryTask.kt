package com.one.word.data.task.dictionary

import com.one.task.Task
import com.one.word.entities.TextLevel

interface DictionaryTask : Task<DictionaryTask.Param, List<TextLevel>> {

    data class Param(val text: String, val inputCode: String, val outputCode: String)
}