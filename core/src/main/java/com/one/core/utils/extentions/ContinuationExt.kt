package com.one.core.utils.extentions

import kotlinx.coroutines.CancellableContinuation

inline fun <T> CancellableContinuation<T>.resumeActive(value: T): Unit = if (!isCompleted)
    resumeWith(Result.success(value))
else
    Unit