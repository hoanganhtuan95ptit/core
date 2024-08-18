package com.simple.detect.mlkit.data.tasks.lanugage.japan

import com.simple.detect.mlkit.data.tasks.lanugage.LanguageSupportTask

interface JapaneseTask : LanguageSupportTask {

    override fun checkSupport(languageCode: String): Boolean {

        return languageCode in listOf("ja")
    }
}