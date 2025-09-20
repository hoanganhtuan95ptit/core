package com.simple.detect_2.mlkit.language.japan

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class JapaneseDetectTask : MlkitDetectTask() {

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return JapaneseTextRecognizerOptions.Builder().build()
    }
}