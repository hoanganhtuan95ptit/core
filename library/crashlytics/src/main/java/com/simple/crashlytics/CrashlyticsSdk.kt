package com.simple.crashlytics

import android.util.Log
import com.simple.job.JobQueueManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.coroutines.CoroutineContext

private const val CRASHLYTICS = "CRASHLYTICS"

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    Log.d(CRASHLYTICS, "error: ", throwable)
}


fun logCrashlytics(event: String, throwable: Throwable, vararg params: Pair<String, String>) = JobQueueManager.submit(CRASHLYTICS, handler) {

    if (throwable is CancellationException) return@submit

    getKoin().getAll<Crashlytics>().map { it.execute(event, throwable, *params) }
}