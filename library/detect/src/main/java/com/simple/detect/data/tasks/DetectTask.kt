package com.simple.detect.data.tasks

import com.simple.detect.entities.DetectOption
import com.simple.detect.entities.Paragraph
import com.simple.task.Task

interface DetectTask : Task<DetectTask.Param, List<Paragraph>> {

    data class Param(val source: Any, val inputCode: String, val outputCode: String, val detectOption: DetectOption, val sizeMax: Int)
}