package com.one.crashlytics

interface Crashlytics {

    suspend fun execute(throwable: Throwable)
}