package com.one.word.data.task.spelling

import com.one.coreapp.utils.extentions.Task
import com.one.word.entities.Spelling

interface SpellingTask : Task<SpellingTask.Param, List<Spelling>> {

    data class Param(val text: String, val inputCode: String)
}