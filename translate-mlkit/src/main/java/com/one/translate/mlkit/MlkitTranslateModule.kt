package com.one.translate.mlkit

import android.app.Application
import com.one.coreapp.Module
import com.one.translate.HandleTranslate
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

private val module = module {

    single { MlkitHandleTranslate() } bind HandleTranslate::class
}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}

private fun injectModule() = loadKoinModules

class MlkitTranslateModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}
