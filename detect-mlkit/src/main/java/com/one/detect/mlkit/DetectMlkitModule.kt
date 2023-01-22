package com.one.detect.mlkit

import android.app.Application
import com.one.coreapp.Module
import com.one.detect.DetectTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module


private val module = module {

    single { ChinaMlkitDetectTask() } bind DetectTask::class

    single { LatinMlkitDetectTask() } bind DetectTask::class

    single { KoreanMlkitDetectTask() } bind DetectTask::class

    single { JapaneseMlkitDetectTask() } bind DetectTask::class

    single { DevanagariMlkitDetectTask() } bind DetectTask::class

}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}


private fun injectModule() = loadKoinModules


class MlkitDetectModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}