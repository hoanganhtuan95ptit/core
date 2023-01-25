package com.one.crashlytics.firebase

import android.app.Application
import com.one.coreapp.Module
import com.one.coreapp.data.task.crashlytics.Crashlytics
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

private val module = module {

    single { FirebaseCrashlytics() } bind Crashlytics::class
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
