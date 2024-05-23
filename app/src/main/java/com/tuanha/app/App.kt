package com.tuanha.app

import android.app.Application
import com.simple.analytics.logAnalytics
import com.tuanha.app.di.appModule
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {

        startKoin {
            appModule
        }

        logAnalytics("test" to "test")

        super.onCreate()
    }
}