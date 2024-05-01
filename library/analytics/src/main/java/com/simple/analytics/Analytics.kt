package com.simple.analytics

interface Analytics {

    suspend fun execute(vararg params: Pair<String, String>)
}