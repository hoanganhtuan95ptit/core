package com.one.analytics.firebase

import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.one.coreapp.BaseApp
import com.one.analytics.Analytics
import com.one.state.ResultState

class FirebaseAnalytics : Analytics {

    override suspend fun execute(param: Analytics.Param): ResultState<Unit> {

        if (BuildConfig.DEBUG) Log.d("tuanha1", "FirebaseAnalytics execute: $param")

        FirebaseAnalytics.getInstance(BaseApp.shared).logEvent(param.name, bundleOf("data" to param.data))

        return ResultState.Success(Unit)
    }
}