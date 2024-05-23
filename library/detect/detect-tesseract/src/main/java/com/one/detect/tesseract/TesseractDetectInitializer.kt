package com.one.detect.tesseract

import android.content.Context
import androidx.startup.Initializer
import com.one.detect.tesseract.data.task.TesseractDetectTask
import com.simple.detect.data.tasks.DetectTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class TesseractDetectInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { TesseractDetectTask(context) } bind DetectTask::class
        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}