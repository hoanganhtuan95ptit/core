package com.hoanganhtuan95ptit.startapp

import android.annotation.SuppressLint
import android.app.Activity
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("StaticFieldLeak")
object StartApp {

    val activityFlow = MutableStateFlow<Activity?>(null)
}