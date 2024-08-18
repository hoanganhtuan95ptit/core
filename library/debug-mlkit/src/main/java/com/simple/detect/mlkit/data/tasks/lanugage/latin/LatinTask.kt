package com.simple.detect.mlkit.data.tasks.lanugage.latin

import com.simple.detect.mlkit.data.tasks.lanugage.LanguageSupportTask

interface LatinTask : LanguageSupportTask {

    override fun checkSupport(languageCode: String): Boolean {

        return languageCode in listOf(
            "latin", "af", "sq", "ca", "hr", "cs", "da", "nl", "en", "et", "fil",
            "tl", "fi", "fr", "de", "hu", "is", "id", "it", "lv", "lt",
            "ms", "no", "pl", "pt", "ro", "sr-Latn", "sk", "sl", "es",
            "sv", "tr", "vi"
        )
    }
}