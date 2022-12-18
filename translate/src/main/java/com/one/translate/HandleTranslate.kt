package com.one.translate

interface HandleTranslate {

    fun priority(): Int = 0

    suspend fun handle(text: String, inputCode: String, outputCode: String): String?
}