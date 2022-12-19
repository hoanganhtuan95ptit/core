package com.one.translate.mlkit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage.*
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.one.coreapp.utils.extentions.resumeActive
import com.one.translate.HandleTranslate
import kotlinx.coroutines.suspendCancellableCoroutine

class MlkitHandleTranslate : HandleTranslate {


    private val map: Map<String, String> = hashMapOf(
    )

    override suspend fun handle(text: String, inputCode: String, outputCode: String): String? {

        val sourceLanguage = map[inputCode] ?: inputCode

        val targetLanguage = map[outputCode] ?: outputCode


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()


        val translator = Translation.getClient(options)


        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()


        val downloadState = suspendCancellableCoroutine<Boolean> { continuation ->

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener {

                continuation.resumeActive(true)
            }.addOnFailureListener {

                Log.d("tuanha", "handle: downloadState", it)
                continuation.resumeActive(false)
            }
        }


        if (!downloadState) {
            return null
        }


        val translate = suspendCancellableCoroutine<String?> { continuation ->

            translator.translate(text).addOnSuccessListener { translatedText ->

                continuation.resumeActive(translatedText)
            }.addOnFailureListener {

                Log.d("tuanha", "handle: translate", it)
                continuation.resumeActive(null)
            }
        }

        return translate
    }

}