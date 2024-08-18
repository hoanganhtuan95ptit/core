package com.simple.detect.mlkit.data.tasks.lanugage.devanagar

import com.simple.detect.mlkit.data.tasks.lanugage.LanguageSupportTask

interface DevanagariTask : LanguageSupportTask {

    override fun checkSupport(languageCode: String): Boolean {

        return languageCode in listOf("hi", "gu")
    }
}