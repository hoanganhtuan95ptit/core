package com.simple.config

import com.simple.task.Task

interface Config : Task<Config.Param, String> {

    data class Param(val key: String, val default: String = "", val timeout: Long = 20 * 1000)
}
