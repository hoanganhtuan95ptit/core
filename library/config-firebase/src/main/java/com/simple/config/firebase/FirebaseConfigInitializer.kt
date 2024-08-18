package com.simple.config.firebase

import android.content.Context
import androidx.startup.Initializer
import com.simple.config.Config
import com.simple.crashlytics.Crashlytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class FirebaseConfigInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { FirebaseConfig() } bind Config::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}