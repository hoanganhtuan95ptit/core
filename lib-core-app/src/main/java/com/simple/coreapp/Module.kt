package com.simple.coreapp

import android.app.Application
import org.koin.core.component.KoinComponent

interface Module : KoinComponent {

    fun init(application: Application)
}