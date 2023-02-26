package com.one.coreapp.data.task.analytics

import com.one.coreapp.data.usecase.ResultState

interface Analytics {

    suspend fun execute(param: Param): ResultState<Unit>

    data class Param(val name: String, val data: String = "")
}