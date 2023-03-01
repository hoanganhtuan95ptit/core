package com.one.detect.mlkit

import android.app.Application
import com.one.coreapp.Module
import com.one.detect.DetectTask
import com.one.detect.mlkit.data.task.*
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module


private val module = module {


    single { MlkitDetectTask(getAll()) } bind DetectTask::class


    single { ChinaMlkitDetectTask() } bind MlkitTask::class

    single { LatinMlkitDetectTask() } bind MlkitTask::class

    single { KoreanMlkitDetectTask() } bind MlkitTask::class

    single { JapaneseMlkitDetectTask() } bind MlkitTask::class

    single { DevanagariMlkitDetectTask() } bind MlkitTask::class
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