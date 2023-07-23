package com.one.crashlytics

import com.one.state.ResultState

interface Crashlytics {

    suspend fun execute(param: Param): ResultState<Unit>

    data class Param(val throwable: Throwable)
}