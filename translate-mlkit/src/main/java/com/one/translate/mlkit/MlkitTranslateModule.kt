package com.one.translate.mlkit

import android.app.Application
import com.one.coreapp.Module
import com.one.translate.TranslateTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

private val module = module {

    single { MlkitTranslateTask() } bind TranslateTask::class
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
