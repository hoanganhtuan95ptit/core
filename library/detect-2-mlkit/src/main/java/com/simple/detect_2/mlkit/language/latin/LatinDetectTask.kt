package com.simple.detect_2.mlkit.language.latin

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class LatinDetectTask : MlkitDetectTask() {

    override fun isSupport(languageCode: String): Boolean {

        return languageCode.lowercase() in listOf(
            "latin", "af", "sq", "ca", "hr", "cs", "da", "nl", "en", "et", "fil",
            "tl", "fi", "fr", "de", "hu", "is", "id", "it", "lv", "lt",
            "ms", "no", "pl", "pt", "ro", "sr-latn", "sk", "sl", "es",
            "sv", "tr", "vi"
        )
    }

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return TextRecognizerOptions.DEFAULT_OPTIONS
    }
}