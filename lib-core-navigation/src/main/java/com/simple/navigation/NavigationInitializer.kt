package com.simple.navigation

import android.content.Context
import androidx.startup.Initializer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class NavigationInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            viewModel { NavigationViewModel(getAll()) }
        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}