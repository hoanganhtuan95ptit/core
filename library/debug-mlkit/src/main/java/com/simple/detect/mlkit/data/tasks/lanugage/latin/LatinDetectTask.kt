package com.simple.detect.mlkit.data.tasks.lanugage.latin

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.simple.detect.mlkit.data.tasks.lanugage.LanguageDetectTask

class LatinDetectTask : LanguageDetectTask(), LatinTask {

    override fun process(inputImage: InputImage): Task<Text> {

        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(inputImage)
    }
}