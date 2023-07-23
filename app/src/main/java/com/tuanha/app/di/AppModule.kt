package com.tuanha.app.di

import com.one.analytics.Analytics
import com.tuanha.app.data.analytics.LogAnalytics
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
//
//    single { WordModule() } bind Module::class
//
//
//    single { FirebaseConfigModule() } bind Module::class
//
//    single { FirebaseAnalyticsModule() } bind Module::class
//
//    single { FirebaseCrashlyticsModule() } bind Module::class
//
//
//    single { TranslateModule() } bind Module::class
//
//    single { MlkitTranslateModule() } bind Module::class
//
//
//    single { DetectModule() } bind Module::class
//
//    single { MlkitDetectModule() } bind Module::class

    single { LogAnalytics() } bind Analytics::class
}