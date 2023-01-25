package com.one.coreapp.data.task.analytics

import com.one.coreapp.utils.extentions.Task

interface Analytics : Task<Analytics.Param, Unit> {

    data class Param(val name: String, val data: String = "")
}