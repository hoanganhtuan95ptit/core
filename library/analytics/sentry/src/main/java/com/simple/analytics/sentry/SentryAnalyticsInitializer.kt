package com.simple.analytics.sentry

import android.content.Context
import androidx.startup.Initializer
import com.simple.analytics.Analytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class SentryAnalyticsInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { SentryAnalytics() } bind Analytics::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}