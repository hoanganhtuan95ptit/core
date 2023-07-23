package com.one.crashlytics.firebase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.one.crashlytics.Crashlytics
import com.one.state.ResultState

class FirebaseCrashlytics : Crashlytics {

    override suspend fun execute(param: Crashlytics.Param): ResultState<Unit> {

        if (BuildConfig.DEBUG) Log.d("tuanha1", "FirebaseCrashlytics execute:", param.throwable)

        FirebaseCrashlytics.getInstance().recordException(param.throwable)

        return ResultState.Success(Unit)
    }
}