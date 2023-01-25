package com.one.coreapp.data.task.config

import com.one.coreapp.utils.extentions.Task

interface Config : Task<Config.Param, String> {

    data class Param(val key: String, val default: String = "", val timeOut: Long = 20 * 1000)
}
