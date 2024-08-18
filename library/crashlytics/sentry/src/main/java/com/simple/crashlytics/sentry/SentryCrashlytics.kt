package com.simple.crashlytics.sentry

import com.simple.crashlytics.Crashlytics
import io.sentry.Sentry
import java.util.UUID

internal val sentrySession by lazy {
    UUID.randomUUID().toString()
}

class SentryCrashlytics : Crashlytics {

    override suspend fun execute(eventName: String, throwable: Throwable, vararg params: Pair<String, String>) {

        val map = hashMapOf("eventName" to eventName, *params)

        Sentry.withScope { scope ->

            scope.setTransaction(sentrySession)

            scope.fingerprint = listOf(throwable.message)

            Sentry.captureException(RuntimeException(map.toString(), throwable))
        }
    }
}