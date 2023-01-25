package com.one.crashlytics.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.one.coreapp.data.task.crashlytics.Crashlytics
import com.one.coreapp.data.usecase.ResultState

class FirebaseCrashlytics : Crashlytics {

    override suspend fun execute(param: Crashlytics.Param): ResultState<Unit> {

        FirebaseCrashlytics.getInstance().recordException(param.throwable)

        return ResultState.Success(Unit)
    }
}