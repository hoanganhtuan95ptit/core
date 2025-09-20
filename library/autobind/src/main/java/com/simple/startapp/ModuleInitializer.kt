package com.simple.startapp

import android.app.Application

interface ModuleInitializer {

    fun create(application: Application)
}