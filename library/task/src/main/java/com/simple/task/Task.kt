package com.simple.task

import com.simple.analytics.logAnalytics
import com.simple.crashlytics.logCrashlytics
import com.simple.state.ResultState
import com.simple.state.isFailed
import com.simple.state.isSuccess
import com.simple.state.toFailed
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.max


interface Task<Param, Result> {

    fun priority(): Int = 0

    suspend fun execute(param: Param): ResultState<Result> {

        val taskId = UUID.randomUUID().toString()

        return runCatching {

            logStart(param, taskId)

            val state = ResultState.Success(executeTask(param))

            logSuccess(param, taskId)

            state
        }.getOrElse {

            logFailed(param, taskId, it)

            ResultState.Failed(it)
        }
    }

    suspend fun tag(): String {
        return this.javaClass.simpleName
    }

    suspend fun logStart(param: Param, taskId: String) {

        logAnalytics("${tag()}_${taskId}_start")
    }

    suspend fun logSuccess(param: Param, taskId: String) {

        logAnalytics("${tag()}_${taskId}_success")
    }

    suspend fun logFailed(param: Param, taskId: String, throwable: Throwable) {

        if (throwable !is LowException) logCrashlytics("${tag()}_${taskId}_failed", throwable)
    }

    suspend fun executeTask(param: Param): Result {

        error("need override")
    }
}


suspend fun <Param, Result> List<Task<Param, Result>>.executeSyncByPriority(param: Param): ResultState<Result> = channelFlow {

    if (isEmpty()) {

        offerActiveAwait(ResultState.Failed(RuntimeException("task empty")))
        return@channelFlow
    }

    val outputs = sortedByDescending {

        it.priority()
    }.map {

        val state = it.execute(param)

        if (state.isSuccess()) {

            offerActiveAwait(state)
            return@channelFlow
        }

        state
    }


    if (outputs.all { it.isFailed() }) {

        offerActive(outputs.minByOrNull { if (it.toFailed()?.cause !is LowException) 0 else 1 }!!)
    }


    awaitClose {
    }
}.first()


suspend fun <Param, Result> List<Task<Param, Result>>.executeAsyncByPriority(param: Param): ResultState<Result> = channelFlow {

    if (isEmpty()) {

        offerActiveAwait(ResultState.Failed(RuntimeException("task empty")))
        return@channelFlow
    }

    val listDeferred = sortedByDescending {

        it.priority()
    }.map {

        async { it.execute(param) }
    }


    val job = launch {

        val outputs = arrayListOf<ResultState<Result>>()

        for (deferred in listDeferred) deferred.await().let {

            if (it.isSuccess()) {

                offerActive(it)
                return@launch
            }

            outputs.add(it)
        }

        if (outputs.all { it.isFailed() }) {

            offerActive(outputs.minByOrNull { if (it.toFailed()?.cause !is LowException) 0 else 1 }!!)
        }
    }


    awaitClose {

        job.cancel()

        listDeferred.map { it.cancel() }
    }
}.first()


suspend fun <Param, Result> List<Task<Param, Result>>.executeAsyncByFast(param: Param): ResultState<Result> = channelFlow {

    if (isEmpty()) {

        offerActiveAwait(ResultState.Failed(RuntimeException("task empty")))
        return@channelFlow
    }

    val listDeferred: Collection<Deferred<ResultState<Result>>> = sortedByDescending {

        it.priority()
    }.map {

        async {

            val output = it.execute(param)

            if (output.isSuccess()) {

                offerActive(output)
            }

            output
        }
    }

    val job = launch {

        val outputs = listDeferred.awaitAll()

        if (outputs.all { it.isFailed() }) {

            offerActive(outputs.minByOrNull { if (it.toFailed()?.cause !is LowException) 0 else 1 }!!)
        }
    }

    awaitClose {

        job.cancel()

        listDeferred.map { it.cancel() }
    }
}.first()

suspend fun <Param, Result> List<Task<Param, Result>>.executeAsyncAll(param: Param) = channelFlow {

    if (isEmpty()) {

        offerActiveAwait(ResultState.Failed(RuntimeException("task empty")))
        return@channelFlow
    }

    val listDeferred = sortedByDescending {

        it.priority()
    }.map {

        async {

            it.execute(param)
        }
    }

    val job = launch {

        offerActive(ResultState.Success(listDeferred.awaitAll()))
    }

    awaitClose {

        job.cancel()

        listDeferred.map { it.cancel() }
    }
}


class LowException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)


private suspend fun <T> ProducerScope<T>.offerActiveAwait(t: T, start: Long = -1, max: Long = -1) {

    if (start > 0 && max > 0) {
        delay(max(0, max - System.currentTimeMillis() + start))
    }

    offerActive(t)

    awaitClose { }
}

private fun <T> ProducerScope<T>.offerActive(t: T) {

    if (isActive) trySend(t)
}
