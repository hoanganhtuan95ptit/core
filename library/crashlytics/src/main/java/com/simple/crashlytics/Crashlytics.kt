package com.simple.crashlytics

interface Crashlytics {

    suspend fun execute(eventName: String, throwable: Throwable, vararg params: Pair<String, String>)
}