package com.simple.detect_2.mlkit.tasks.japan

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.tasks.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class JapaneseDetectTask : MlkitDetectTask() {

    override fun isSupportLanguage(languageCode: String): Boolean {
        return languageCode in listOf("ja")
    }

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return JapaneseTextRecognizerOptions.Builder().build()
    }
}