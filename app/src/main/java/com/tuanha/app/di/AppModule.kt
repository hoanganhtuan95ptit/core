package com.tuanha.app.di

import com.one.coreapp.Module
import com.one.translate.TranslateModule
import com.one.translate.mlkit.MlkitTranslateModule
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single { TranslateModule() } bind Module::class

    single { MlkitTranslateModule() } bind Module::class
}