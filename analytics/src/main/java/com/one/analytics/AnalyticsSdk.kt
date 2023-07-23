package com.one.analytics

import android.util.Log
import com.four.job.JobQueueManager
import com.one.core.utils.extentions.normalize
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.coroutines.CoroutineContext

private const val ANALYTICS = "ANALYTICS"

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    Log.d("Analytics", "error: ", throwable)
}

fun logAnalytics(name: String, data: String = "") = JobQueueManager.submit(ANALYTICS, handler) {

    val eventName = name.normalize().replace(".", "").replace(" ", "_").replace("-", "_")

    val listAnalytics = getKoin().getAll<Analytics>()
    listAnalytics.map { it.execute(Analytics.Param(eventName, data)) }
}