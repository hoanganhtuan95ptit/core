package com.simple.detect.mlkit

import android.content.Context
import androidx.startup.Initializer
import com.simple.detect.data.usecase.tasks.DetectTask
import com.simple.detect.mlkit.data.tasks.mlkit.LatinMlkitDetectTask
import com.simple.detect.mlkit.data.tasks.MlkitDetectTask
import com.simple.detect.mlkit.data.tasks.mlkit.MlkitTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class MlkitDetectInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { MlkitDetectTask(getAll()) } bind DetectTask::class


//            single { ChinaMlkitDetectTask() } bind MlkitTask::class

            single { LatinMlkitDetectTask() } bind MlkitTask::class

//            single { KoreanMlkitDetectTask() } bind MlkitTask::class
//
//            single { JapaneseMlkitDetectTask() } bind MlkitTask::class
//
//            single { DevanagariMlkitDetectTask() } bind MlkitTask::class
        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}