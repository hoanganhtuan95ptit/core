//package com.tuanha.detect.mlkit.latin
//
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.text.TextRecognition
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions
//import com.one.coreapp.utils.extentions.toBitmap
//import com.one.detect.HandleDetect
//import kotlinx.coroutines.suspendCancellableCoroutine
//
//class LatinMlkitHandleDetect : HandleDetect {
//
//    override suspend fun handle(path: String, inputCode: String, outputCode: String): String? {
//
//        if (inputCode in listOf("")) {
//
//            return null
//        }
//
//        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//
//
//        val bitmap = path.toBitmap()
//
//
//        val a = suspendCancellableCoroutine<> {
//
//            recognizer.process(InputImage.fromBitmap(bitmap, 0)).addOnSuccessListener { visionText ->
//
//            }.addOnFailureListener { e ->
//
//            }
//        }
//
//        return translate
//    }
//
//}