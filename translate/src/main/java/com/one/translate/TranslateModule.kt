package com.one.translate

import android.app.Application
import com.one.coreapp.Module
import com.one.translate.data.usecase.TranslateUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val translateModule = module {

    single {
        TranslateUseCase(getAll())
    }

}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            translateModule,
        )
    )
}

private fun injectTranslateModule() = loadKoinModules


class TranslateModule : Module {

    override fun init(application: Application) {
        injectTranslateModule()
    }
}