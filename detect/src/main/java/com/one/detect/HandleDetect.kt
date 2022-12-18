package com.one.detect

interface HandleDetect {

    fun priority(): Int = 0

    suspend fun handle(path: String, inputCode: String, outputCode: String): String?
}