package com.simple.detect.mlkit.data.tasks.lanugage.china

import com.simple.detect.mlkit.data.tasks.lanugage.LanguageSupportTask

interface ChinaTask : LanguageSupportTask {

    override fun checkSupport(languageCode: String): Boolean {

        return languageCode in listOf("zh-CN", "zh-TW", "zh")
    }
}