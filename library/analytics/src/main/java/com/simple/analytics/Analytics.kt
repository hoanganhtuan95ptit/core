package com.simple.analytics

interface Analytics {

    suspend fun execute(eventName: String, vararg params: Pair<String, String>)
}