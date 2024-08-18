package com.simple.detect.mlkit.data.tasks.lanugage.korean

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.simple.detect.mlkit.data.tasks.lanugage.LanguageDetectTask

class KoreanDetectTask : LanguageDetectTask(), KoreanTask {

    override fun process(inputImage: InputImage): Task<Text> {

        return TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()).process(inputImage)
    }
}