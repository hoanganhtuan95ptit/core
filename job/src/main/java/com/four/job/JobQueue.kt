package com.four.job

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


private val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->

    Log.d("JobQueue", "error: ", throwable)
}

private class JobQueue {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + handler)
    private val queue = Channel<Job>(Channel.UNLIMITED)

    init {
        scope.launch(Dispatchers.Default) {
            for (job in queue) job.join()
        }
    }

    fun submit(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {
        val job = scope.launch(context, CoroutineStart.LAZY, block)
        queue.trySend(job)
    }

    fun cancel() {
        queue.cancel()
        scope.cancel()
    }
}

object JobQueueManager {

    private val concurrentHashMap = ConcurrentHashMap<String, JobQueue>()

    fun submit(tag: String, context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {

        val jobQueue = concurrentHashMap[tag] ?: JobQueue().apply {
            concurrentHashMap[tag] = this
        }

        jobQueue.submit(context, block)
    }

    internal fun cancel() {

        concurrentHashMap.values.map { it.cancel() }
    }
}
