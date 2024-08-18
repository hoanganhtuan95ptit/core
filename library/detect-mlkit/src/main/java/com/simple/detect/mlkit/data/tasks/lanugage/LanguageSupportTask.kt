package com.simple.detect.mlkit.data.tasks.lanugage

interface LanguageSupportTask {

    fun checkSupport(languageCode: String): Boolean
}