package com.one.coreapp.data.task.crashlytics

import com.one.coreapp.utils.extentions.Task

interface Crashlytics : Task<Crashlytics.Param, Unit> {

    data class Param(val throwable: Throwable)
}