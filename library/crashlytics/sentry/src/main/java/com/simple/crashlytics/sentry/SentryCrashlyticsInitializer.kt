package com.simple.crashlytics.sentry

import android.content.Context
import androidx.startup.Initializer
import com.simple.analytics.Analytics
import com.simple.crashlytics.Crashlytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class SentryCrashlyticsInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { SentryCrashlytics() } bind Crashlytics::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}