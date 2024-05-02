package com.simple.detect.mlkit.data.tasks.mlkit//package com.simple.detect.mlkit.data.task
//
//import com.google.android.gms.tasks.Task
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.text.Text
//import com.google.mlkit.vision.text.TextRecognition
//import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
//import com.simple.detect.mlkit.data.task.MlkitDetectTask
//
//class ChinaMlkitDetectTask : MlkitTask() {
//
//    override fun process(inputImage: InputImage): Task<Text> {
//
//        return TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build()).process(inputImage)
//    }
//}