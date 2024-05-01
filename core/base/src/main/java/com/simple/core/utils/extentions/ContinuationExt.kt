package com.simple.core.utils.extentions

import kotlinx.coroutines.CancellableContinuation

fun <T> CancellableContinuation<T>.resumeActive(value: T): Unit = if (!isCompleted)
    resumeWith(Result.success(value))
else
    Unit