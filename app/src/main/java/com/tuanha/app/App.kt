package com.tuanha.app

import android.app.Application
import android.content.Context

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        start

        super.attachBaseContext(base)
    }

    override fun onCreate() {

//        startKoin {
//            appModule
//        }
//
//        logAnalytics("test" to "test")

        super.onCreate()
    }

    companion object{

        val start = System.currentTimeMillis()
    }
}