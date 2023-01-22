package com.one.detect

import com.one.coreapp.utils.extentions.Task
import com.one.detect.entities.TextBlock

interface DetectTask : Task<DetectTask.Param, List<TextBlock>> {

    data class Param(val path: String, val inputCode: String, val outputCode: String)

}