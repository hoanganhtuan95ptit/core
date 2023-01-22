package com.one.detect.mlkit

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.one.detect.mlkit.MlkitDetectTask

class KoreanMlkitDetectTask : MlkitDetectTask() {

    override fun inputCodeSupport(): List<String> = listOf("ko")

    override fun process(inputImage: InputImage): Task<Text> {
        return TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()).process(inputImage)
    }
}