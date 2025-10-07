package com.simple.startapp

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.startup.Initializer
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.simple.autobind.AutoBind
import com.simple.startapp.StartApp.activityResumeFlow
import com.simple.startapp.StartApp.applicationFlow
import kotlinx.coroutines.launch

class StartAppInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        if (context !is Application) return

        applicationFlow.value = context


        AutoBind.init(context)


        val listener = SplitInstallStateUpdatedListener { state ->

            if (state.status() == SplitInstallSessionStatus.INSTALLED) {

                AutoBind.forceLoad(100)
            }
        }

        SplitInstallManagerFactory.create(context).registerListener(listener)


        context.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                activityResumeFlow.value = activity
            }

            override fun onActivityPaused(activity: Activity) {
                if (activityResumeFlow.value == activity) activityResumeFlow.value = null
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        });

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
