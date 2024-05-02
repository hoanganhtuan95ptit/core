package com.simple.crashlytics.firebase

import android.content.Context
import androidx.startup.Initializer
import com.simple.crashlytics.Crashlytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class FirebaseCrashlyticsInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { FirebaseCrashlytics(get()) } bind Crashlytics::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}