package com.one.detect

import com.one.detect.entities.DetectOption
import com.one.detect.entities.Paragraph
import com.one.task.Task

interface DetectTask : Task<DetectTask.Param, List<Paragraph>> {

    data class Param(val source: Any, val inputCode: String, val outputCode: String, val detectOption: DetectOption, val sizeMax: Int)
}