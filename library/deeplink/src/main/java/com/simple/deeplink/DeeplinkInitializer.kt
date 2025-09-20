package com.simple.deeplink

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.startup.Initializer
import com.simple.autobind.AutoBind
import com.simple.deeplink.queue.DeeplinkQueue
import com.simple.deeplink.utils.exts.launchCollect
import kotlinx.coroutines.Job

class DeeplinkInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        (context as? Application)?.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                if (activity !is ComponentActivity) return

                var jobs: List<Job>? = null

                AutoBind.loadAsync(DeeplinkQueue::class.java).launchCollect(activity) { list ->

                    jobs?.map { it.cancel() }
                    jobs = list.mapNotNull { it.setupDeepLink(activity) }
                }

                if (activity is FragmentActivity) activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {

                    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {

                        var jobs: List<Job>? = null

                        AutoBind.loadAsync(DeeplinkQueue::class.java).launchCollect(f) { list ->

                            jobs?.map { it.cancel() }
                            jobs = list.mapNotNull { it.setupDeepLink(f) }
                        }
                    }

                }, true)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
