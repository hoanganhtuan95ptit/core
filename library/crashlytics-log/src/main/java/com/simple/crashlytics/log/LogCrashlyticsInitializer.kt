package com.simple.crashlytics.log

import android.content.Context
import androidx.startup.Initializer
import com.simple.crashlytics.Crashlytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class LogCrashlyticsInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { LogCrashlytics() } bind Crashlytics::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}