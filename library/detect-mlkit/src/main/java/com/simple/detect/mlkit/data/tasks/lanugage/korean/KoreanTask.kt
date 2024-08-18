package com.simple.detect.mlkit.data.tasks.lanugage.korean

import com.simple.detect.mlkit.data.tasks.lanugage.LanguageSupportTask

interface KoreanTask : LanguageSupportTask {

    override fun checkSupport(languageCode: String): Boolean {

        return languageCode in listOf("ko")
    }
}