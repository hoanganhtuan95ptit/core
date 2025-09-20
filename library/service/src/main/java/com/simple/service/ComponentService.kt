@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.simple.service

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import androidx.fragment.app.Fragment

interface ComponentService<T : ComponentCallbacks> {

    fun priority(): Int = 0

    suspend fun setup(t: T)
}

interface ApplicationService : ComponentService<Application> {

    override suspend fun setup(application: Application)
}

interface ActivityService : ComponentService<Activity> {

    override suspend fun setup(activity: Activity)
}

interface FragmentService : ComponentService<Fragment> {

    override suspend fun setup(fragment: Fragment)
}