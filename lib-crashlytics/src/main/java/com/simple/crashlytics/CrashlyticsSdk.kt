package com.simple.crashlytics

import android.util.Log
import com.simple.job.JobQueueManager
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.coroutines.CoroutineContext

private const val CRASHLYTICS = "CRASHLYTICS"

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    Log.d(CRASHLYTICS, "error: ", throwable)
}

fun logCrashlytics(throwable: Throwable) = JobQueueManager.submit(CRASHLYTICS, handler) {

    getKoin().getAll<Crashlytics>().map { it.execute(throwable) }
}