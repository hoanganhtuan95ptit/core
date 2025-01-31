package com.simple.analytics.firebase

import android.content.Context
import androidx.startup.Initializer
import com.simple.analytics.Analytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class FirebaseAnalyticsInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { FirebaseAnalytics() } bind Analytics::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}