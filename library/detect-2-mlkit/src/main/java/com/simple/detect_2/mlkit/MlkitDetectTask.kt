package com.simple.detect_2.mlkit

import android.graphics.Bitmap
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.simple.detect_2.DetectTask
import com.simple.detect_2.entities.Paragraph
import com.simple.detect_2.entities.Sentence
import com.simple.detect_2.entities.Word
import com.simple.state.ResultState
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first

abstract class MlkitDetectTask() : DetectTask {

    protected abstract fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface

    override suspend fun detect(source: Bitmap): List<Paragraph> {

        val state = detectAwait(source = source)

        if (state is ResultState.Failed) {

            throw state.cause
        }

        if (state !is ResultState.Success) {

            return emptyList()
        }

        return state.data.wrapTextBlock()
    }

    private suspend fun detectAwait(source: Bitmap) = channelFlow {

        TextRecognition.getClient(textRecognizerOptionsInterface()).process(InputImage.fromBitmap(source, 0)).addOnSuccessListener { visionText ->

            val textBlockList = visionText.textBlocks

            if (textBlockList.isEmpty()) {

                trySend(ResultState.Failed(RuntimeException()))
            } else {

                trySend(ResultState.Success(textBlockList))
            }
        }.addOnFailureListener { e ->

            trySend(ResultState.Failed(e))
        }
    }.first()

    private fun List<Text.TextBlock>.wrapTextBlock() = map { _paragraph ->

        val paragraph = Paragraph()

        paragraph.sentences = _paragraph.lines.wrapLine()

        paragraph.text = paragraph.sentences.joinToString(separator = "\n") {

            it.text
        }

        paragraph.points = _paragraph.cornerPoints?.toList()

        paragraph
    }

    private fun List<Text.Line>.wrapLine() = map { _sequence ->

        val sequence = Sentence()

        sequence.words = _sequence.elements.wrapElement()

        sequence.text = sequence.words.joinToString(separator = " ") {

            it.text
        }

        sequence.angle = _sequence.angle
        sequence.points = _sequence.cornerPoints?.toList()
        sequence.confidence = _sequence.confidence

        sequence
    }

    private fun List<Text.Element>.wrapElement() = map { _word ->

        val word = Word()

        word.text = _word.symbols.joinToString(separator = "") {

            it.text
        }

        word.angle = _word.angle
        word.points = _word.cornerPoints?.toList()
        word.confidence = _word.confidence

        word
    }

    private suspend fun identifyLanguage(text: String) = channelFlow {

        val options = LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.34f).build()

        LanguageIdentification.getClient(options).identifyLanguage(text).addOnSuccessListener { languageCode ->

            trySend(languageCode.lowercase())
        }.addOnFailureListener {

            trySend("")
        }
    }.first()

    private fun String.wrap(): String {
        return this
    }
}