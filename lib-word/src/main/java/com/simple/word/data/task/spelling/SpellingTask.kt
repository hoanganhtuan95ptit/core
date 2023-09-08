package com.simple.word.data.task.spelling

import com.simple.task.Task
import com.simple.word.entities.Spelling

interface SpellingTask : Task<SpellingTask.Param, List<Spelling>> {

    data class Param(val text: String, val inputCode: String)
}