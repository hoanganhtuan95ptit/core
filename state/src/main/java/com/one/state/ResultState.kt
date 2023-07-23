package com.one.state


open class ResultState<out T> {

    object Start : ResultState<Nothing>()

    data class Running<T>(val data: T) : ResultState<T>()

    data class Success<T>(val data: T) : ResultState<T>()

    data class Failed(val error: Throwable) : ResultState<Nothing>()
}


fun <T> ResultState<T>.toStart() = this as? ResultState.Start

fun <T> ResultState<T>.toFailed() = this as? ResultState.Failed

fun <T> ResultState<T>.toRunning() = this as? ResultState.Running

fun <T> ResultState<T>.toSuccess() = this as? ResultState.Success


fun <T> ResultState<T>.isStart() = this is ResultState.Start

fun <T> ResultState<T>.isFailed() = this is ResultState.Failed

fun <T> ResultState<T>.isRunning() = this is ResultState.Running

fun <T> ResultState<T>.isSuccess() = this is ResultState.Success

fun <T> ResultState<T>.isCompleted() = isSuccess() || isFailed()


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
    action.invoke(it.error)
}