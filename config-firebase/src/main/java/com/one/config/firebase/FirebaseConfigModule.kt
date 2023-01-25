package com.one.config.firebase

import android.app.Application
import com.one.coreapp.Module
import com.one.coreapp.data.task.config.Config
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

private val module = module {

    single { FirebaseConfig() } bind Config::class
}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}


private fun injectModule() = loadKoinModules


class FirebaseConfigModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}
