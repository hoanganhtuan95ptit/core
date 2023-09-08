package com.simple.translate.mlkit

import android.content.Context
import androidx.startup.Initializer
import com.simple.translate.TranslateTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class MlkitTranslateInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { MlkitTranslateTask() } bind TranslateTask::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}