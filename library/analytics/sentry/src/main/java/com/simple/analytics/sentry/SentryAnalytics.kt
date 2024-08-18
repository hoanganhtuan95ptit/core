package com.simple.analytics.sentry

import com.simple.analytics.Analytics
import io.sentry.Sentry

class SentryAnalytics : Analytics {

    override suspend fun execute(eventName: String, vararg params: Pair<String, String>) {

        Sentry.withScope { scope ->

            scope.fingerprint = params.toMap().keys.toList()

            params.toMap().map {
                scope.setExtra(it.key, it.value)
            }

            Sentry.captureMessage(eventName)
        }
    }
}