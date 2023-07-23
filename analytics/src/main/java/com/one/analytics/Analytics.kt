package com.one.analytics

import com.one.state.ResultState

interface Analytics {

    suspend fun execute(param: Param): ResultState<Unit>

    data class Param(val name: String, val data: String = "")
}