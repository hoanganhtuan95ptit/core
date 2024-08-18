package com.simple.analytics.log

import android.annotation.SuppressLint
import com.simple.analytics.Analytics

class LogAnalytics : Analytics {

    @SuppressLint("MissingPermission")
    override suspend fun execute(eventName: String, vararg params: Pair<String, String>) {

        android.util.Log.d("LogAnalytics", "eventName:$eventName params:$params")
    }
}