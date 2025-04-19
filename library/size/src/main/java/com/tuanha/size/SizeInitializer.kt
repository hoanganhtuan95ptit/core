package com.tuanha.size

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.startup.Initializer
import com.simple.core.utils.extentions.asObject
import com.simple.core.utils.extentions.asObjectOrNull

class SizeInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        context.asObject<Application>().registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                activity.asObjectOrNull<AppCompatActivity>()?.setupSize()
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