package com.one.crashlytics

import com.four.job.JobQueueManager
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.coroutines.CoroutineContext

private const val CRASHLYTICS = "CRASHLYTICS"

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    if (BuildConfig.DEBUG) throwable.printStackTrace()
}

fun logCrashlytics(throwable: Throwable) = JobQueueManager.submit(CRASHLYTICS, handler) {

    val listAnalytics = getKoin().getAll<Crashlytics>()
    listAnalytics.map { it.execute(Crashlytics.Param(throwable)) }
}