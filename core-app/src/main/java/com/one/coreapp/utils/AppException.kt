package com.one.coreapp.utils

class AppException(val code: String, var title: String? = null, override val message: String = "", override val cause: Throwable? = null, val image: Int = -1) : RuntimeException(message, cause)