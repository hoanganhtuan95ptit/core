package com.simple.detect_2.mlkit.language.devanagar

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class DevanagariDetectTask : MlkitDetectTask() {

    override fun isSupport(languageCode: String): Boolean {

       return languageCode in listOf("hi", "gu")
    }

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return DevanagariTextRecognizerOptions.Builder().build()
    }
}