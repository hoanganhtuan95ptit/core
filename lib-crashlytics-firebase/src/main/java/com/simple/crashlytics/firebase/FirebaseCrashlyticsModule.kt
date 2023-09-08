package com.simple.crashlytics.firebase

import android.app.Application
import com.simple.coreapp.Module
import com.simple.crashlytics.Crashlytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

private val module = module {

    single { FirebaseCrashlytics(get()) } bind Crashlytics::class
}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}


private fun injectModule() = loadKoinModules


class FirebaseCrashlyticsModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}
