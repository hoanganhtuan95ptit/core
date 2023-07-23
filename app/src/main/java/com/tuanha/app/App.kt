package com.tuanha.app

import com.one.analytics.logAnalytics
import com.one.coreapp.BaseApp
import com.one.coreapp.Module
import com.tuanha.app.di.appModule
import org.koin.android.ext.android.getKoin
import org.koin.core.context.loadKoinModules

class App : BaseApp() {

    private val modules by lazy {
        getKoin().getAll<Module>()
    }

    override fun onCreate() {

        loadKoinModules(
            listOf(
                appModule
            )
        )

        logAnalytics("test", data = "test")

        super.onCreate()

        modules.forEach { it.init(this) }
    }

}