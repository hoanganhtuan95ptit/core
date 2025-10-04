package com.simple.detect_2.mlkit.language.korean

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class KoreanDetectTask : MlkitDetectTask() {

    override fun isSupport(languageCode: String): Boolean {

        return languageCode in listOf("ko")
    }

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return KoreanTextRecognizerOptions.Builder().build()
    }
}