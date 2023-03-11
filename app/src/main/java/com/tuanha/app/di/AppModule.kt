package com.tuanha.app.di

import com.one.analytics.firebase.FirebaseAnalyticsModule
import com.one.config.firebase.FirebaseConfigModule
import com.one.coreapp.Module
import com.one.crashlytics.firebase.FirebaseCrashlyticsModule
import com.one.detect.DetectModule
import com.one.detect.mlkit.MlkitDetectModule
import com.one.translate.TranslateModule
import com.one.translate.mlkit.MlkitTranslateModule
import com.one.word.WordModule
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single { WordModule() } bind Module::class


    single { FirebaseConfigModule() } bind Module::class

    single { FirebaseAnalyticsModule() } bind Module::class

    single { FirebaseCrashlyticsModule() } bind Module::class


    single { TranslateModule() } bind Module::class

    single { MlkitTranslateModule() } bind Module::class


    single { DetectModule() } bind Module::class

    single { MlkitDetectModule() } bind Module::class
}