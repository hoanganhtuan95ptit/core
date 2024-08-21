package com.simple.translate

import android.content.Context
import androidx.startup.Initializer
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class TranslateInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}