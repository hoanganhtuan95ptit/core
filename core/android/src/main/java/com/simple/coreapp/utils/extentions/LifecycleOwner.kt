package com.simple.coreapp.utils.extentions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.simple.job.JobQueueManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun LifecycleOwner.launchQueue(tag: String = "ui", context: CoroutineContext = EmptyCoroutineContext, action: suspend CoroutineScope.() -> Unit) {

    lifecycleScope.launch {

        JobQueueManager.submit(tag, lifecycleScope.coroutineContext + context) {
            action.invoke(this)
        }
    }
}