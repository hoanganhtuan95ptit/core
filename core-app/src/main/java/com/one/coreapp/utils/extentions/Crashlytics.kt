package com.one.coreapp.utils.extentions

import com.one.coreapp.App
import com.one.coreapp.BuildConfig
import com.one.coreapp.data.task.crashlytics.Crashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    if (BuildConfig.DEBUG) throwable.printStackTrace()
}

fun logException(throwable: Throwable) = GlobalScope.launch(handler + Dispatchers.IO) {

    App.shared.logCrashlytics.executeByFast(Crashlytics.Param(throwable))
}