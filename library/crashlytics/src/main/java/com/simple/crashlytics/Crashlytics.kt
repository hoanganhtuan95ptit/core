package com.simple.crashlytics

interface Crashlytics {

    suspend fun execute(throwable: Throwable)
}