package com.one.coreapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class BaseApp : MultiDexApplication() {

    companion object {

        var numStart: Int = 0

        lateinit var shared: BaseApp
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        startKoin {
            androidContext(this@BaseApp)
            androidLogger(Level.NONE)
        }
    }

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
