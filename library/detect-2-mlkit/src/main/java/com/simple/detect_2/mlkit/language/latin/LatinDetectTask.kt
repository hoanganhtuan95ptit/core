package com.simple.detect_2.mlkit.language.latin

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class LatinDetectTask : MlkitDetectTask() {

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return TextRecognizerOptions.DEFAULT_OPTIONS
    }
}