package com.one.detect

import com.one.coreapp.utils.extentions.Task
import com.one.detect.entities.DetectOption
import com.one.detect.entities.Paragraph
import com.one.detect.entities.TextBlock

interface DetectTask : Task<DetectTask.Param, List<Paragraph>> {

    data class Param(val path: String, val inputCode: String, val outputCode: String, val detectOption: DetectOption)
}