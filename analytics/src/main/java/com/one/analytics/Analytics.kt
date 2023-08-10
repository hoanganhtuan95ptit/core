package com.one.analytics

interface Analytics {

    suspend fun execute(vararg params: Pair<String, String>)
}