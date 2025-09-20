package com.simple.detect_2.mlkit.language.china

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class ChinaDetectTask : MlkitDetectTask() {

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return ChineseTextRecognizerOptions.Builder().build()
    }
}