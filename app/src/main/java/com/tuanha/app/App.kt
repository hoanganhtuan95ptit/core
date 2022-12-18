package com.tuanha.app

import com.one.coreapp.App
import com.one.coreapp.Module
import com.tuanha.app.di.appModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : App() {

    private val modules by lazy {
        getKoin().getAll<Module>()
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger(Level.NONE)

            modules(
                appModule
            )
        }

        modules.forEach { it.init(this) }
    }

}