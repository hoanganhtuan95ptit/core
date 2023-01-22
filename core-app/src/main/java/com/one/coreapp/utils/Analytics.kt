package com.one.coreapp.utils

import android.annotation.SuppressLint
import android.os.Bundle
import com.one.core.utils.extentions.normalize
import com.one.coreapp.App
import com.one.coreapp.BuildConfig
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@SuppressLint("MissingPermission")
object Analytics {

    val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
        if (BuildConfig.DEBUG) throwable.printStackTrace()
    }

    fun log(name: String, any: String = name) = GlobalScope.launch(handler + Dispatchers.IO) {

        val eventName = name.normalize().replace(".", "").replace("-", "_")

        val bundle = Bundle()
        bundle.putString("data", any)

        App.shared.analytics.map { it.logEvent(eventName, bundle) }
    }

    fun logException(throwable: Throwable) = GlobalScope.launch(handler + Dispatchers.IO) {

        App.shared.analytics.map { it.logException(throwable) }
    }

}
