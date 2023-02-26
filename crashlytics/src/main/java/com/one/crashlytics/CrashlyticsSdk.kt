package com.one.coreapp.utils.extentions

import com.one.coreapp.data.task.crashlytics.Crashlytics
import com.one.crashlytics.BuildConfig
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    if (BuildConfig.DEBUG) throwable.printStackTrace()
}

private var crashlyticsList: List<Crashlytics>? = null


object CrashlyticsSdk {

    fun init(logCrashlytics: List<Crashlytics>) {

        crashlyticsList = logCrashlytics
    }
}


fun logException(throwable: Throwable) = GlobalScope.launch(handler + Dispatchers.IO) {

    crashlyticsList?.map { it.execute(Crashlytics.Param(throwable)) }
}