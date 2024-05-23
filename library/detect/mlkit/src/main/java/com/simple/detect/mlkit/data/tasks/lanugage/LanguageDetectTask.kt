package com.simple.detect.mlkit.data.tasks.lanugage

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.simple.state.ResultState
import com.simple.task.Task
import com.simple.crashlytics.logCrashlytics
import com.simple.coreapp.utils.extentions.resumeActive
import com.simple.task.LowException
import kotlinx.coroutines.suspendCancellableCoroutine


abstract class LanguageDetectTask : Task<LanguageDetectTask.Param, List<Text.TextBlock>>, LanguageSupportTask {

    abstract fun process(inputImage: InputImage): com.google.android.gms.tasks.Task<Text>

    override suspend fun execute(param: Param): ResultState<List<Text.TextBlock>> = suspendCancellableCoroutine { continuation ->

        if (!checkSupport(param.inputCode)) {

            continuation.resumeActive(ResultState.Failed(LowException(this.javaClass.simpleName)))
            return@suspendCancellableCoroutine
        }

        process(InputImage.fromBitmap(param.bitmap, 0)).addOnSuccessListener { visionText ->

            val textBlockList = visionText.textBlocks

            if (textBlockList.isEmpty()) {

                continuation.resumeActive(ResultState.Failed(RuntimeException(this.javaClass.simpleName)))
            } else {

                continuation.resumeActive(ResultState.Success(textBlockList))
            }
        }.addOnFailureListener { e ->

            logCrashlytics(RuntimeException(this.javaClass.simpleName, e))

            continuation.resumeActive(ResultState.Failed(e))
        }
    }

    data class Param(val bitmap: Bitmap, val inputCode: String)
}