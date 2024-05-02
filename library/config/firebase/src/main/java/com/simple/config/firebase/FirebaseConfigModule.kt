package com.simple.config.firebase

import android.app.Application
import com.simple.config.Config
import com.simple.coreapp.Module
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
