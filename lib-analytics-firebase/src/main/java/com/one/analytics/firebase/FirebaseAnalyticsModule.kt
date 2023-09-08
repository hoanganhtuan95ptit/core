package com.one.analytics.firebase

import android.app.Application
import com.one.coreapp.Module
import com.one.analytics.Analytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

private val module = module {


    single { FirebaseAnalytics() } bind Analytics::class
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
