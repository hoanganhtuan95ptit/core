package com.tuanha.app

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hoanganhtuan95ptit.autobind.AutoBind
import com.hoanganhtuan95ptit.startapp.ModuleInitializer
import com.simple.coreapp.utils.ext.launchCollect

class App : Application() {

    override fun attachBaseContext(base: Context?) {
        start

        super.attachBaseContext(base)


        AutoBind.loadAsync(ModuleInitializer::class.java, true).launchCollect(ProcessLifecycleOwner.get().lifecycleScope) {
            Log.d("tuanha", "attachBaseContext: ${System.currentTimeMillis() - start}")
        }
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