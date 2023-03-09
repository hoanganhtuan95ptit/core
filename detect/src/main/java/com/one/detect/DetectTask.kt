package com.one.detect

import com.one.task.Task
import com.one.detect.entities.DetectOption
import com.one.detect.entities.Paragraph

interface DetectTask : Task<DetectTask.Param, List<Paragraph>> {

    data class Param(val path: String, val inputCode: String, val outputCode: String, val detectOption: DetectOption, val sizeMax: Int)
}