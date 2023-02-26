package com.one.coreapp.utils.extentions

import com.one.analytics.BuildConfig
import com.one.core.utils.extentions.normalize
import com.one.coreapp.data.task.analytics.Analytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    if (BuildConfig.DEBUG) throwable.printStackTrace()
}

private var analyticsList: List<Analytics>? = null


object AnalyticsSdk {

    fun init(logAnalytics: List<Analytics>) {

        analyticsList = logAnalytics
    }
}


fun log(name: String, data: String = "") = GlobalScope.launch(handler + Dispatchers.IO) {

    val eventName = name.normalize().replace(".", "").replace(" ", "_").replace("-", "_")

    analyticsList?.map { it.execute(Analytics.Param(eventName, data)) }
}