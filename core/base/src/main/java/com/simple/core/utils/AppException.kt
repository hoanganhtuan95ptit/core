package com.simple.core.utils

class AppException(
    val code: String,
    override val message: String = "",
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)