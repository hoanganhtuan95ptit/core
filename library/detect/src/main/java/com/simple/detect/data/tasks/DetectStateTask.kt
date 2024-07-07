package com.simple.detect.data.tasks

import com.simple.task.Task

interface DetectStateTask : Task<DetectStateTask.Param, Pair<String, Int>> {

    data class Param(val languageCode: String)
}