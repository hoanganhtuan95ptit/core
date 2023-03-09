package com.one.detect.tesseract

import android.app.Application
import com.one.coreapp.Module
import com.one.detect.DetectTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module


private val module = module {

    single { TesseractDetectTask() } bind DetectTask::class
}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}


private fun injectModule() = loadKoinModules


class TesseractDetectModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}