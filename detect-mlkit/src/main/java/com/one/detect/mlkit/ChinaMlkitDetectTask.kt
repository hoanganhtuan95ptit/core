package com.one.detect.mlkit

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions

class ChinaMlkitDetectTask : MlkitDetectTask() {

    override fun inputCodeSupport(): List<String> = listOf("zh", "zh-Hans", "Zzh", "zh-Hant–HK")

    override fun process(inputImage: InputImage): Task<Text> {
        return TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build()).process(inputImage)
    }
}