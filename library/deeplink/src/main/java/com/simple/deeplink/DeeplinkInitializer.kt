package com.simple.deeplink

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.startup.Initializer

class DeeplinkInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        (context as? Application)?.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                groupQueue.forEach { it.setupDeepLink(activity) }

                if (activity is FragmentActivity) activity.supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->

                    groupQueue.forEach { it.setupDeepLink(fragment) }
                }
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
