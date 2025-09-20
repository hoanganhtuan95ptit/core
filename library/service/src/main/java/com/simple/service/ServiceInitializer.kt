package com.simple.service

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.startup.Initializer
import com.simple.autobind.AutoBind
import com.simple.autobind.utils.exts.createObject
import com.unknown.coroutines.launchCollect
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn

class ServiceInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        if (context !is Application) return

        setupApplication(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    private fun setupApplication(context: Application) {

        AutoBind.loadAsync(ApplicationService::class.java, true).launchCollect(ProcessLifecycleOwner.get()) { list ->

            list.sortedBy { it.priority() }.map { it.setup(context) }
        }

        context.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                setupActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun setupActivity(activity: Activity) {

        AutoBind.loadAsync(ActivityService::class.java, true).launchCollect(ProcessLifecycleOwner.get()) { list ->

            list.sortedBy { it.priority() }.map { it.setup(activity) }
        }

        AutoBind.loadNameAsync(activity.javaClass, true).launchCollect(ProcessLifecycleOwner.get()) { list ->

            list.mapNotNull { it.createObject(ActivityService::class.java) }.sortedBy { it.priority() }.map { it.setup(activity) }
        }

        if (activity is FragmentActivity) activity.fragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {

            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {

                setupFragment(f)
            }
        })
    }

    private fun setupFragment(fragment: Fragment) {

        AutoBind.loadAsync(FragmentService::class.java, true).launchCollect(ProcessLifecycleOwner.get()) { list ->

            list.sortedBy { it.priority() }.map { it.setup(fragment) }
        }

        AutoBind.loadNameAsync(fragment.javaClass, true).launchCollect(ProcessLifecycleOwner.get()) { list ->

            list.mapNotNull { it.createObject(FragmentService::class.java) }.sortedBy { it.priority() }.map { it.setup(fragment) }
        }
    }

    private fun FragmentActivity.fragmentLifecycleCallbacks(fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks) = channelFlow<Unit> {

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true)

        awaitClose {

            supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        }
    }.launchIn(this.lifecycleScope)
}
