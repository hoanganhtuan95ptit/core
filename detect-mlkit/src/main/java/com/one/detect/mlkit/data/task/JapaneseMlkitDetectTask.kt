package com.one.detect.mlkit.data.task

import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions

class JapaneseMlkitDetectTask : MlkitTask() {

    override fun process(inputImage: InputImage): Task<Text> {

        return TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build()).process(inputImage)
    }
}