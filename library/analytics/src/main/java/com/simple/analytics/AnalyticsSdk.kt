package com.simple.analytics

import android.util.Log
import com.simple.job.JobQueueManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.coroutines.CoroutineContext

private const val ANALYTICS = "ANALYTICS"

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    Log.d(ANALYTICS, "error: ", throwable)
}

fun logAnalytics(eventName: String, vararg params: Pair<String, String>) = JobQueueManager.submit(ANALYTICS, handler + Dispatchers.IO) {

    getKoin().getAll<Analytics>().map { it.execute(eventName, *params) }
}