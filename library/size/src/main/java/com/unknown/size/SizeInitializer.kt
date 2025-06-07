package com.unknown.size

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.startup.Initializer


class SizeInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        if (context is Application) context.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                if (activity is FragmentActivity) setupSize(activity)
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        });

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
