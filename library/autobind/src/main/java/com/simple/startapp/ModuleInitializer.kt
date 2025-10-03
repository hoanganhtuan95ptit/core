package com.simple.startapp

import android.app.Application

@Deprecated("use ApplicationService")
interface ModuleInitializer {

    fun create(application: Application)
}