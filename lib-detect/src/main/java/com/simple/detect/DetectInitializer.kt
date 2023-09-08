package com.simple.detect

import android.content.Context
import androidx.startup.Initializer
import com.simple.detect.data.usecase.DetectUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class DetectInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { DetectUseCase(getAll()) }

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}