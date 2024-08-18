package com.simple.detect.data.tasks

import com.simple.detect.entities.DetectProvider
import com.simple.detect.entities.DetectState
import com.simple.task.Task

interface DetectStateTask : Task<DetectStateTask.Param, Pair<DetectProvider, DetectState>> {

    data class Param(val languageCode: String)
}