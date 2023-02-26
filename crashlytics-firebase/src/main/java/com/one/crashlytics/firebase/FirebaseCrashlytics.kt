package com.one.crashlytics.firebase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.one.coreapp.data.task.crashlytics.Crashlytics
import com.one.coreapp.data.usecase.ResultState

class FirebaseCrashlytics : Crashlytics {

    override suspend fun execute(param: Crashlytics.Param): ResultState<Unit> {

        if (BuildConfig.DEBUG) Log.d("tuanha", "FirebaseCrashlytics execute:", param.throwable)

        FirebaseCrashlytics.getInstance().recordException(param.throwable)

        return ResultState.Success(Unit)
    }
}