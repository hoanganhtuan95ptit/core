package com.one.detect

import android.app.Application
import com.one.coreapp.Module
import com.one.detect.data.usecase.DetectUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val module = module {

    single {
        DetectUseCase(getAll())
    }

}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}


private fun injectModule() = loadKoinModules


class DetectModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}