package com.tuanha.app.data.analytics

import android.util.Log
import com.simple.analytics.Analytics
import com.simple.analytics.BuildConfig
import com.simple.core.utils.extentions.toJson

class LogAnalytics : Analytics {

    override suspend fun execute(vararg params: Pair<String, String>) {

        if (BuildConfig.DEBUG) Log.d("tuanha1", "Analytics: ${params.toMap().toJson()}")
    }
}