package com.unknown.coroutines

import com.simple.crashlytics.logCrashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
    logCrashlytics("error captured", throwable)
}

