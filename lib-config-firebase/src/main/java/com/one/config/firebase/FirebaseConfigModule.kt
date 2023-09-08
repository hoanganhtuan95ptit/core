package com.one.config.firebase

import android.app.Application
import com.four.config.Config
import com.one.coreapp.Module
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
