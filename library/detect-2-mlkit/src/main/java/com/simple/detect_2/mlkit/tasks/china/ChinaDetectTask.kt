package com.simple.detect_2.mlkit.tasks.china

import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.simple.autobind.annotation.AutoBind
import com.simple.detect_2.DetectTask
import com.simple.detect_2.mlkit.tasks.MlkitDetectTask

@AutoBind(MlkitDetectTask::class, DetectTask::class)
class ChinaDetectTask : MlkitDetectTask() {

    override fun isSupportLanguage(languageCode: String): Boolean {
        return languageCode.lowercase() in listOf("zh-cn", "zh-tw", "zh")
    }

    override fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface {
        return ChineseTextRecognizerOptions.Builder().build()
    }
}