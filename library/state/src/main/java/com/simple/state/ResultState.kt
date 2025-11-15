package com.simple.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


sealed class ResultState<out T> {

    object Start : ResultState<Nothing>()

    data class Running<T>(val data: T) : ResultState<T>()

    data class Success<T>(val data: T) : ResultState<T>()

    data class Failed(val cause: Throwable = RuntimeException("")) : ResultState<Nothing>()
}


fun <T> ResultState<T>.toStart() = this as? ResultState.Start

fun <T> ResultState<T>.toFailed() = this as? ResultState.Failed

fun <T> ResultState<T>.toRunning() = this as? ResultState.Running

fun <T> ResultState<T>.toSuccess() = this as? ResultState.Success


fun <T> ResultState<T>?.isStart() = this is ResultState.Start

fun <T> ResultState<T>?.isFailed() = this is ResultState.Failed

fun <T> ResultState<T>?.isRunning() = this is ResultState.Running

fun <T> ResultState<T>?.isSuccess() = this is ResultState.Success

fun <T> ResultState<T>?.isCompleted() = isSuccess() || isFailed()


inline fun <T> ResultState<T>.doStart(action: () -> Unit) = toStart()?.let {
    action.invoke()
}

inline fun <T> ResultState<T>.doSuccess(action: (T) -> Unit) = toSuccess()?.data?.let {
    action.invoke(it)
}

inline fun <T> ResultState<T>.doRunning(action: (T) -> Unit) = toRunning()?.data?.let {
    action.invoke(it)
}

inline fun <T> ResultState<T>.doHasData(action: (T) -> Unit) = (toRunning()?.data ?: toSuccess()?.data)?.let {
    action.invoke(it)
}

inline fun <T> ResultState<T>.doFailed(action: (error: Throwable) -> Unit) = toFailed()?.let {
    action.invoke(it.cause)
}


inline fun <T, R> T.runResultState(block: T.() -> R) = runCatching {
    ResultState.Success(block.invoke(this))
}.getOrElse {
    ResultState.Failed(it)
}


inline fun <T, R> ResultState<T>.map(block: (T) -> R): ResultState<R> = if (this is ResultState.Running) runCatching {

    ResultState.Running(data = block.invoke(this.data))
}.getOrElse {

    ResultState.Failed(it)
} else if (this is ResultState.Success) runCatching {

    ResultState.Success(data = block.invoke(this.data))
}.getOrElse {

    ResultState.Failed(it)
} else if (this is ResultState.Failed) {

    this
} else if (this is ResultState.Start) {

    this
} else {

    ResultState.Failed(RuntimeException("not support wrap ${this.javaClass.simpleName}"))
}

inline fun <T, R> ResultState<T>.mapToState(block: (T) -> ResultState<R>): ResultState<R> = if (this is ResultState.Running) runCatching {

    block.invoke(this.data)
}.getOrElse {

    ResultState.Failed(it)
} else if (this is ResultState.Success) runCatching {

    block.invoke(this.data)
}.getOrElse {

    ResultState.Failed(it)
} else if (this is ResultState.Failed) {

    this
} else if (this is ResultState.Start) {

    this
} else {

    ResultState.Failed(RuntimeException("not support wrap ${this.javaClass.simpleName}"))
}

inline fun <T, R> ResultState<T>.mapToFlowState(block: (T) -> Flow<ResultState<R>>): Flow<ResultState<R>> = if (this is ResultState.Running) runCatching {

    block.invoke(this.data)
}.getOrElse {

    flowOf(ResultState.Failed(it))
} else if (this is ResultState.Success) runCatching {

    block.invoke(this.data)
}.getOrElse {

    flowOf(ResultState.Failed(it))
} else if (this is ResultState.Failed) {

    flowOf(this)
} else if (this is ResultState.Start) {

    flowOf(this)
} else {

    flowOf(ResultState.Failed(RuntimeException("not support wrap ${this.javaClass.simpleName}")))
}


fun <T, R> Flow<ResultState<T>>.mapToData(block: suspend (T) -> R): Flow<ResultState<R>> = map { state ->

    state.map {
        block.invoke(it)
    }
}

fun <T, R> Flow<ResultState<T>>.mapToState(block: suspend (T) -> ResultState<R>): Flow<ResultState<R>> = map { state ->

    state.mapToState {
        block.invoke(it)
    }
}

fun <T, R> Flow<ResultState<T>>.flatMapLatestState(block: suspend (T) -> Flow<ResultState<R>>): Flow<ResultState<R>> = flatMapLatest { state ->

    state.mapToFlowState {
        block.invoke(it)
    }
}