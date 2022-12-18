package com.one.coreapp

import android.app.Application

interface Module {

    fun init(application: Application)
}