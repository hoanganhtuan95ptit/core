package com.simple.translate.mlkit

import android.content.Context
import androidx.startup.Initializer
import com.simple.translate.data.tasks.TranslateStateTask
import com.simple.translate.data.tasks.TranslateTask
import com.simple.translate.mlkit.data.tasks.MlkitTranslateStateTask
import com.simple.translate.mlkit.data.tasks.MlkitTranslateTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class MlkitTranslateInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { MlkitTranslateTask() } bind TranslateTask::class

            single { MlkitTranslateStateTask() } bind TranslateStateTask::class
        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}