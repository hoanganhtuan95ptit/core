package com.simple.crashlytics.sentry

import com.simple.crashlytics.Crashlytics
import io.sentry.Sentry
import java.lang.RuntimeException

class SentryCrashlytics : Crashlytics {

    override suspend fun execute(throwable: Throwable) {

        Sentry.captureException(RuntimeException("wrap", throwable))
    }
}