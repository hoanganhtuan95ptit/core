package com.simple.crashlytics.sentry

import com.simple.crashlytics.Crashlytics
import io.sentry.Sentry
import java.util.UUID

internal val sentrySession by lazy {
    UUID.randomUUID().toString()
}

class SentryCrashlytics : Crashlytics {

    override suspend fun execute(throwable: Throwable) {

        Sentry.withScope { scope ->

            scope.setTransaction(sentrySession)

            scope.fingerprint = listOf(throwable.message)

            Sentry.captureException(throwable)
        }
    }
}