package com.one.detect.mlkit

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.one.detect.mlkit.MlkitDetectTask

class JapaneseMlkitDetectTask : MlkitDetectTask() {

    override fun inputCodeSupport(): List<String> = listOf("ja")

    override fun process(inputImage: InputImage): Task<Text> {
        return TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build()).process(inputImage)
    }
}