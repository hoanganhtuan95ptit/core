package com.unknown.string

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hoanganhtuan95ptit.autobind.annotation.AutoBind
import com.hoanganhtuan95ptit.startapp.ModuleInitializer
import com.hoanganhtuan95ptit.startapp.StartApp
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AutoBind(ModuleInitializer::class)
class StringInitializer : ModuleInitializer {

    override fun create(context: Context) {

        if (context !is Application) return

        ProcessLifecycleOwner.get().lifecycleScope.launch {

            setupString(StartApp.activityFlow.filterIsInstance<FragmentActivity>().first())
        }

        context.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                if (activity is FragmentActivity) setupString(activity)
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}
