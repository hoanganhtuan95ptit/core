package com.one.coreapp

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.newSingleThreadContext

open class App : MultiDexApplication() {

    companion object {
        lateinit var shared: App

        var numStart: Int = 0
    }

    val dispatcherForHandleDataUi = newSingleThreadContext("handle_data_for_ui")

    override fun onCreate() {
        shared = this

        super.onCreate()

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
