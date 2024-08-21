package com.simple.crashlytics.log

import com.simple.core.utils.extentions.toJson
import com.simple.crashlytics.Crashlytics

class LogCrashlytics() : Crashlytics {

    override suspend fun execute(eventName: String, throwable: Throwable, vararg params: Pair<String, String>) {

        android.util.Log.d("LogCrashlytics", "eventName:${eventName} params:${params.toJson()}", throwable)
    }
}