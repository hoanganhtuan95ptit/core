package com.tuanha.app.data.analytics

import android.util.Log
import com.one.analytics.Analytics
import com.one.analytics.BuildConfig
import com.one.core.utils.extentions.toJson
import com.one.state.ResultState

class LogAnalytics : Analytics {

    override suspend fun execute(param: Analytics.Param): ResultState<Unit> {

        if (BuildConfig.DEBUG) Log.d("tuanha1", "LogAnalytics execute: ${param.toJson()}")

        return ResultState.Success(Unit)
    }
}