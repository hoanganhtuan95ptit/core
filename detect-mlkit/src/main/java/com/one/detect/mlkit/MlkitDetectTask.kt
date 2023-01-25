package com.one.detect.mlkit

import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.data.usecase.toSuccess
import com.one.coreapp.utils.extentions.log
import com.one.coreapp.utils.extentions.logException
import com.one.coreapp.utils.extentions.resumeActive
import com.one.coreapp.utils.extentions.toBitmap
import com.one.detect.DetectTask
import com.one.detect.entities.*
import kotlinx.coroutines.suspendCancellableCoroutine

abstract class MlkitDetectTask : DetectTask {

    override suspend fun execute(param: DetectTask.Param): ResultState<List<Paragraph>> {


        if (!param.detectOption.isDetectText()) {

            return ResultState.Failed(RuntimeException("not support ${param.detectOption.name}"))
        }


        val inputCodeSupport = inputCodeSupport()


        if (inputCodeSupport.isNotEmpty() && inputCodeSupport.all { !it.equals(param.inputCode, true) }) {

            return ResultState.Failed(RuntimeException("not support ${param.inputCode}"))
        }


        log("MlkitDetectTask", "")


        val bitmap = param.path.toBitmap(width = param.sizeMax, height = param.sizeMax)


        val state = suspendCancellableCoroutine<ResultState<List<Paragraph>>> { continuation ->

            process(InputImage.fromBitmap(bitmap, 0)).addOnSuccessListener { visionText ->

                val textBlockList = visionText.textBlocks.map { _paragraph ->

                    val paragraph = Paragraph()
                    paragraph.languageCode = _paragraph.recognizedLanguage

                    paragraph.sentences = _paragraph.lines.map { _sequence ->

                        val sequence = Sentence()
                        sequence.languageCode = _sequence.recognizedLanguage

                        sequence.words = _sequence.elements.map { _word ->

                            val word = Word()
                            word.languageCode = _word.recognizedLanguage

                            word.text = _word.symbols.joinToString(separator = "") {

                                it.text
                            }
                            word.rect = _word.symbols.mapNotNull { it.boundingBox }.let { rects ->

                                TextRest(rects.minOf { it.left }, rects.minOf { it.top }, rects.maxOf { it.right }, rects.maxOf { it.bottom })
                            }

                            word
                        }

                        sequence.text = sequence.words.joinToString(separator = " ") {

                            it.text
                        }
                        sequence.rect = sequence.words.mapNotNull { it.rect }.let { rects ->

                            TextRest(rects.minOf { it.left }, rects.minOf { it.top }, rects.maxOf { it.right }, rects.maxOf { it.bottom })
                        }

                        sequence
                    }

                    paragraph.text = paragraph.sentences.joinToString(separator = "\n") {

                        it.text
                    }

                    paragraph.rect = paragraph.sentences.mapNotNull { it.rect }.let { rects ->

                        TextRest(rects.minOf { it.left }, rects.minOf { it.top }, rects.maxOf { it.right }, rects.maxOf { it.bottom })
                    }

                    paragraph
                }

                if (textBlockList.isEmpty()) {

                    continuation.resumeActive(ResultState.Failed(RuntimeException("")))
                } else {

                    continuation.resumeActive(ResultState.Success(textBlockList))
                }
            }.addOnFailureListener { e ->

                logException(RuntimeException("MlkitDetectTask", e))

                continuation.resumeActive(ResultState.Failed(e))
            }
        }


        state.toSuccess()?.data?.forEach { paragraph ->

            if (paragraph.languageCode !in listOf("", "und")) return@forEach

            paragraph.languageCode = identifyLanguage(paragraph.text)


            paragraph.sentences.forEach { sentence ->

                sentence.languageCode = sentence.languageCode.takeIf { it.isNotBlank() } ?: paragraph.languageCode

                sentence.words.forEach { word ->

                    word.languageCode = word.languageCode.takeIf { it.isNotBlank() } ?: paragraph.languageCode
                }
            }
        }


        return state
    }


    private suspend fun identifyLanguage(text: String) = suspendCancellableCoroutine<String> { a ->

        LanguageIdentification.getClient(LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.34f).build()).identifyLanguage(text).addOnSuccessListener { languageCode ->

            a.resumeActive(languageCode.takeIf { !it.equals("und", true) } ?: "")
        }.addOnFailureListener {

            a.resumeActive("")
        }
    }


    open protected fun inputCodeSupport(): List<String> = emptyList()


    open protected fun process(inputImage: InputImage): Task<Text> {

        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(inputImage)
    }

}