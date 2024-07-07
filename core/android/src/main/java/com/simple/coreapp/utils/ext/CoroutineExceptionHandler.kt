package com.simple.coreapp.utils.ext

import android.util.Log
import com.simple.crashlytics.logCrashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
    Log.d("tuanha", ": ", throwable)
    throwable.printStackTrace()
    logCrashlytics(throwable)
}

