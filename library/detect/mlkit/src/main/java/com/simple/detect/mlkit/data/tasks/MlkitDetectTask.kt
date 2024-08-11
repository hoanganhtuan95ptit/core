package com.simple.detect.mlkit.data.tasks

import android.content.Context
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.text.Text
import com.simple.analytics.logAnalytics
import com.simple.core.utils.extentions.resumeActive
import com.simple.core.utils.extentions.toArrayList
import com.simple.core.utils.extentions.validate
import com.simple.detect.data.tasks.DetectTask
import com.simple.detect.entities.Paragraph
import com.simple.detect.entities.Sentence
import com.simple.detect.entities.Word
import com.simple.detect.entities.isDetectText
import com.simple.detect.mlkit.Constants
import com.simple.detect.mlkit.data.tasks.lanugage.LanguageDetectTask
import com.simple.image.toBitmap
import com.simple.state.ResultState
import com.simple.state.isFailed
import com.simple.state.toFailed
import com.simple.state.toSuccess
import com.simple.task.LowException
import com.simple.task.executeAsyncAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine

class MlkitDetectTask(
    private val context: Context,
    private val taskList: List<LanguageDetectTask>,
) : DetectTask {

    override suspend fun executeTask(param: DetectTask.Param): List<Paragraph> {

        if (param.source !is String) {

            throw LowException("not support source ${param.source.javaClass.simpleName}")
        }

        if (!param.detectOption.isDetectText()) {

            throw LowException("not support ${param.detectOption.name}")
        }


        val path = param.source

        val bitmap = path.toBitmap(context = context, width = param.sizeMax, height = param.sizeMax)

        val states = taskList.executeAsyncAll(LanguageDetectTask.Param(bitmap, inputCode = param.inputCode)).first().toSuccess()?.data ?: emptyList()


        if (states.all { it.isFailed() }) {

            throw states.filterIsInstance<ResultState.Failed>().minByOrNull { if (it.toFailed()?.cause !is LowException) 0 else 1 }?.cause ?: error("")
        }


        val stateSuccessList = states.filterIsInstance<ResultState.Success<List<Text.TextBlock>>>().toMutableList()

        val stateSuccessPrimary = stateSuccessList.maxByOrNull { it.data.flatMap { textBlock -> textBlock.lines.flatMap { it.elements } }.size }

        stateSuccessList.remove(stateSuccessPrimary)


        val boxList = stateSuccessPrimary?.data?.mapNotNull { it.boundingBox } ?: emptyList()

        val textBlockList = stateSuccessPrimary?.data?.toArrayList() ?: arrayListOf()


        stateSuccessList.flatMap {

            it.data
        }.forEach { textBlock ->

            if (!boxList.contains(textBlock.boundingBox)) textBlockList.add(textBlock)
        }


        textBlockList.sortBy { it.boundingBox?.bottom }


        val paragraphList = textBlockList.map { _paragraph ->

            val paragraph = Paragraph()


            paragraph.sentences = _paragraph.lines.map { _sequence ->

                val sequence = Sentence()

                sequence.words = _sequence.elements.map { _word ->

                    val word = Word()

                    word.text = _word.symbols.joinToString(separator = "") {

                        it.text
                    }

                    word.angle = _word.angle
                    word.points = _word.cornerPoints?.toList()
                    word.confidence = _word.confidence

                    word
                }

                sequence.text = sequence.words.joinToString(separator = " ") {

                    it.text
                }

                sequence.angle = _sequence.angle
                sequence.points = _sequence.cornerPoints?.toList()
                sequence.confidence = _sequence.confidence

                sequence
            }


            paragraph.text = paragraph.sentences.joinToString(separator = "\n") {

                it.text
            }

            paragraph.points = _paragraph.cornerPoints?.toList()

            paragraph
        }.validate { paragraph ->


            paragraph.languageCode = identifyLanguage(paragraph.text)


            paragraph.sentences.forEach { sentence ->

                sentence.languageCode = sentence.languageCode.takeIf { it.isNotBlank() } ?: paragraph.languageCode

                sentence.words.forEach { word ->

                    word.languageCode = word.languageCode.takeIf { it.isNotBlank() } ?: paragraph.languageCode
                }
            }
        }

        return paragraphList
    }

    private suspend fun identifyLanguage(text: String) = suspendCancellableCoroutine { a ->

        LanguageIdentification.getClient(LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.34f).build()).identifyLanguage(text).addOnSuccessListener { languageCode ->

            a.resumeActive(languageCode.lowercase().replace("-latn", Constants.LATIN).replace("und", Constants.LATIN))
        }.addOnFailureListener {

            a.resumeActive("")
        }
    }
}