package com.one.coreapp

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.one.coreapp.data.task.analytics.Analytics
import com.one.coreapp.data.task.config.Config
import com.one.coreapp.data.task.crashlytics.Crashlytics
import com.one.coreapp.utils.extentions.AnalyticsSdk
import com.one.coreapp.utils.extentions.CrashlyticsSdk
import kotlinx.coroutines.newSingleThreadContext
import org.koin.android.ext.android.getKoin

open class App : MultiDexApplication() {

    companion object {
        lateinit var shared: App

        var numStart: Int = 0
    }

    val configs by lazy {
        getKoin().getAll<Config>()
    }

    val logAnalytics by lazy {
        getKoin().getAll<Analytics>()
    }

    val logCrashlytics by lazy {
        getKoin().getAll<Crashlytics>()
    }


    val dispatcherForHandleDataUi = newSingleThreadContext("handle_data_for_ui")


    override fun onCreate() {
        shared = this

        super.onCreate()

        AnalyticsSdk.init(logAnalytics)

        CrashlyticsSdk.init(logCrashlytics)

        registerActivityLifecycleCallbacks(AppLifeCycle())
    }


    class AppLifeCycle : ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            numStart++
        }

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            numStart--
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }
}
