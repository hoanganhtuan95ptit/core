package com.one.detect.mlkit

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.text.Text
import com.one.core.utils.extentions.toArrayList
import com.one.core.utils.extentions.validate
import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.data.usecase.isFailed
import com.one.coreapp.data.usecase.toFailed
import com.one.coreapp.utils.extentions.*
import com.one.detect.DetectTask
import com.one.detect.entities.*
import com.one.detect.mlkit.data.task.MlkitTask
import com.one.task.LowException
import com.one.task.executeAsyncAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine

class MlkitDetectTask(
    private val taskList: List<MlkitTask>
) : DetectTask {

    override suspend fun execute(param: DetectTask.Param): ResultState<List<Paragraph>> {


        if (!param.detectOption.isDetectText()) {

            return ResultState.Failed(LowException("not support ${param.detectOption.name}"))
        }


        log("MlkitDetectTask", "")


        val path = param.source as? String ?: throw LowException("not support source ${param.source.javaClass.simpleName}")


        val bitmap = path.toBitmap(width = param.sizeMax, height = param.sizeMax)


        val states = taskList.executeAsyncAll(MlkitTask.Param(bitmap)).first()


        if (states.all { it.isFailed() }) {

            return states.filterIsInstance<ResultState.Failed>().minByOrNull { if (it.toFailed()?.error !is LowException) 0 else 1 }!!
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
        }.validate {


            languageCode = identifyLanguage(text)


            sentences.forEach { sentence ->

                sentence.languageCode = sentence.languageCode.takeIf { it.isNotBlank() } ?: languageCode

                sentence.words.forEach { word ->

                    word.languageCode = word.languageCode.takeIf { it.isNotBlank() } ?: languageCode
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