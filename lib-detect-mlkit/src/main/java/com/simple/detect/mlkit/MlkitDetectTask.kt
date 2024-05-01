package com.simple.detect.mlkit

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.text.Text
import com.simple.analytics.logAnalytics
import com.simple.core.utils.extentions.resumeActive
import com.simple.core.utils.extentions.toArrayList
import com.simple.core.utils.extentions.validate
import com.simple.coreapp.utils.extentions.toBitmap
import com.simple.detect.DetectTask
import com.simple.detect.entities.Paragraph
import com.simple.detect.entities.Sentence
import com.simple.detect.entities.TextRest
import com.simple.detect.entities.Word
import com.simple.detect.entities.isDetectText
import com.simple.detect.mlkit.data.task.MlkitTask
import com.simple.state.ResultState
import com.simple.state.isFailed
import com.simple.state.toFailed
import com.simple.state.toSuccess
import com.simple.task.LowException
import com.simple.task.executeAsyncAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine

class MlkitDetectTask(
    private val taskList: List<MlkitTask>
) : DetectTask {

    override suspend fun execute(param: DetectTask.Param): ResultState<List<Paragraph>> {


        if (!param.detectOption.isDetectText()) {

            return ResultState.Failed(LowException("not support ${param.detectOption.name}"))
        }


        logAnalytics("MLKIT_DETECT_TASK" to "MLKIT_DETECT_TASK")


        val path = param.source as? String ?: throw LowException("not support source ${param.source.javaClass.simpleName}")


        val bitmap = path.toBitmap(width = param.sizeMax, height = param.sizeMax)


        val states = taskList.executeAsyncAll(MlkitTask.Param(bitmap)).first().toSuccess()?.data?: emptyList()


        if (states.all { it.isFailed() }) {

            return states.filterIsInstance<ResultState.Failed>().minByOrNull { if (it.toFailed()?.cause !is LowException) 0 else 1 }!!
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
        }.validate { paragraph ->


            paragraph.languageCode = identifyLanguage(paragraph.text)


            paragraph.sentences.forEach { sentence ->

                sentence.languageCode = sentence.languageCode.takeIf { it.isNotBlank() } ?: paragraph.languageCode

                sentence.words.forEach { word ->

                    word.languageCode = word.languageCode.takeIf { it.isNotBlank() } ?: paragraph.languageCode
                }
            }
        }

        return ResultState.Success(paragraphList)
    }


    private suspend fun identifyLanguage(text: String) = suspendCancellableCoroutine<String> { a ->

        LanguageIdentification.getClient(LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.34f).build()).identifyLanguage(text).addOnSuccessListener { languageCode ->

            a.resumeActive(languageCode.lowercase().replace("-latn", "").replace("und", ""))
        }.addOnFailureListener {

            a.resumeActive("")
        }
    }
}