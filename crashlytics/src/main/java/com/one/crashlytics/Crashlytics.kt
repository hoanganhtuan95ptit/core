package com.one.coreapp.data.task.crashlytics

import com.one.coreapp.data.usecase.ResultState

interface Crashlytics {

    suspend fun execute(param: Param): ResultState<Unit>

    data class Param(val throwable: Throwable)
}