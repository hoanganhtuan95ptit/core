package com.simple.analytics.firebase

import android.app.Application
import com.simple.coreapp.Module
import com.simple.analytics.Analytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

private val module = module {


    single { FirebaseAnalytics(get()) } bind Analytics::class
}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}


private fun injectModule() = loadKoinModules


class FirebaseAnalyticsModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}
