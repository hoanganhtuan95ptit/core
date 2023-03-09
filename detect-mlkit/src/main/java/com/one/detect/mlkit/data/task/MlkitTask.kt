package com.one.detect.mlkit.data.task

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.one.coreapp.data.usecase.ResultState
import com.one.task.Task
import com.one.coreapp.utils.extentions.logException
import com.one.coreapp.utils.extentions.resumeActive
import kotlinx.coroutines.suspendCancellableCoroutine

abstract class MlkitTask : Task<MlkitTask.Param, List<Text.TextBlock>> {

    abstract fun process(inputImage: InputImage): com.google.android.gms.tasks.Task<Text>

    override suspend fun execute(param: Param): ResultState<List<Text.TextBlock>> = suspendCancellableCoroutine { continuation ->

        process(InputImage.fromBitmap(param.bitmap, 0)).addOnSuccessListener { visionText ->

            val textBlockList = visionText.textBlocks

            if (textBlockList.isEmpty()) {

                continuation.resumeActive(ResultState.Failed(RuntimeException(this.javaClass.simpleName)))
            } else {

                continuation.resumeActive(ResultState.Success(textBlockList))
            }
        }.addOnFailureListener { e ->

            logException(RuntimeException(this.javaClass.simpleName, e))

            continuation.resumeActive(ResultState.Failed(e))
        }
    }

    data class Param(val bitmap: Bitmap)
}