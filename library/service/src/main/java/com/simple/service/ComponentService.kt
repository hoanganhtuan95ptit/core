@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package com.simple.service

import android.app.Application
import android.content.ComponentCallbacks
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


interface ComponentService<T : ComponentCallbacks> {

    fun priority(): Int = 0

    fun setup(t: T)
}


interface ApplicationService : ComponentService<Application> {

    override fun setup(application: Application)
}


interface ActivityService : ComponentService<FragmentActivity> {

    override fun setup(fragmentActivity: FragmentActivity)
}


interface FragmentCreatedService : ComponentService<Fragment> {

    override fun setup(fragment: Fragment)
}

interface FragmentViewCreatedService : ComponentService<Fragment> {

    override fun setup(fragment: Fragment)
}