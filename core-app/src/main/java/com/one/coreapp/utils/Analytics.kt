package com.one.coreapp.utils

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
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

    val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(App.shared)
    }

    fun log(name: String, any: String = name) = GlobalScope.launch(handler + Dispatchers.IO) {

        val eventName = name.normalize().replace(".", "").replace("-", "_")

        if (BuildConfig.DEBUG) Log.d("tuanha", "$eventName: $any")

        val bundle = Bundle()
        bundle.putString("data", any)

        firebaseAnalytics.logEvent(eventName, bundle)
    }

    fun logException(throwable: Throwable) = GlobalScope.launch(handler + Dispatchers.IO) {

        if (BuildConfig.DEBUG) Log.d("tuanha", "logException: ", throwable)

        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

}
