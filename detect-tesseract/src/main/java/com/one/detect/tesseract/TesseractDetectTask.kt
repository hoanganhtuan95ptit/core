package com.one.detect.tesseract

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.googlecode.tesseract.android.TessBaseAPI
import com.one.core.utils.extentions.toArrayList
import com.one.coreapp.App
import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.data.usecase.isFailed
import com.one.coreapp.data.usecase.toFailed
import com.one.coreapp.utils.extentions.downloadSyncV2
import com.one.coreapp.utils.extentions.getFile
import com.one.coreapp.utils.extentions.resumeActive
import com.one.coreapp.utils.extentions.toBitmap
import com.one.detect.DetectTask
import com.one.detect.entities.*
import com.one.task.LowException
import com.one.task.Task
import com.one.task.executeAsyncAll
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File


class TesseractDetectTask : DetectTask {

    val languageMap = hashMapOf(
        "en" to listOf("eng")
    )

    override suspend fun executeTask(param: DetectTask.Param): List<Paragraph> {


        val bitmap = (param.source as? Bitmap)
            ?: (param.source as? String)?.toBitmap()
            ?: throw  LowException("not support ${param.source.javaClass.simpleName}")


        val child = "tessdata"

        val fileRoot = App.shared.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!

        val languageList = languageMap[param.inputCode]


        languageList?.map { language ->

            GlobalScope.async {

                val file = "$child/$language.traineddata".getFile(fileRoot, false)

                if (file.exists()) return@async

                "$child/$language.traineddata".getFile(fileRoot, true).downloadSyncV2("https://raw.githubusercontent.com/tesseract-ocr/tessdata_best/main/${language}.traineddata")
            }
        }?.awaitAll()


        val list = child.takeIf {

            languageMap[param.inputCode] != null
        }?.getFile(fileRoot, false)?.listFiles()?.toList()?.filter {

            it.absolutePath.endsWith("traineddata")
        }?.takeIf { it.isNotEmpty() }?.map {

            Pair(fileRoot, it.name.replace(".traineddata", ""))
        } ?: languageList?.map { language ->

            Pair(fileRoot, language)
        } ?: throw LowException("not found language")

        list.map {
            Log.d("tuanha", "executeTask: ${it.first.absolutePath}")
        }

        val states = list.map { TesseractTask(fileRoot, it.second) }.executeAsyncAll(TesseractTask.Param(bitmap)).first()


        if (states.all { it.isFailed() }) {

            throw states.filterIsInstance<ResultState.Failed>().minByOrNull { if (it.toFailed()?.error !is LowException) 0 else 1 }!!.error
        }


        val stateSuccessList = states.filterIsInstance<ResultState.Success<List<Paragraph>>>().toMutableList()

        val stateSuccessPrimary = stateSuccessList.maxByOrNull { it.data.flatMap { textBlock -> textBlock.sentences.flatMap { it.words } }.size }

        stateSuccessList.remove(stateSuccessPrimary)


        val boxList = stateSuccessPrimary?.data?.mapNotNull { it.rect } ?: emptyList()

        val textBlockList = stateSuccessPrimary?.data?.toArrayList() ?: arrayListOf()


        stateSuccessList.flatMap {

            it.data
        }.forEach { textBlock ->

            if (!boxList.contains(textBlock.rect)) textBlockList.add(textBlock)
        }


        textBlockList.sortBy { it.rect?.bottom }


        textBlockList.map { paragraph ->

            paragraph.languageCode = identifyLanguage(paragraph.text)

            paragraph.sentences.forEach { sentence ->

                sentence.languageCode = paragraph.languageCode

                sentence.words.forEach { word ->

                    word.languageCode = paragraph.languageCode
                }
            }
        }


        return textBlockList
    }

    private suspend fun identifyLanguage(text: String) = suspendCancellableCoroutine<String> { a ->

        LanguageIdentification.getClient(LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.34f).build()).identifyLanguage(text).addOnSuccessListener { languageCode ->

            a.resumeActive(languageCode.lowercase().replace("-latn", "").replace("und", ""))
        }.addOnFailureListener {

            a.resumeActive("")
        }
    }

    private class TesseractTask(val fileRoot: File, val languageCode: String) : Task<TesseractTask.Param, List<Paragraph>> {

        override suspend fun executeTask(param: Param): List<Paragraph> {


            val tess = TessBaseAPI()

            if (!tess.init(fileRoot.absolutePath, languageCode)) {

                throw LowException("not init ${fileRoot.absolutePath} - $languageCode")
            }


            tess.setImage(param.bitmap)
//            tess.pageSegMode = TessBaseAPI.PageSegMode.PSM_SINGLE_WORD
            tess.utF8Text
            Log.d("tuanha", "executeTask: utF8Text:")


            var sentence = Sentence().apply {
                rect = TextRest(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
            }

            var paragraph = Paragraph().apply {
                rect = TextRest(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
            }


            var wordList: MutableList<Word> = arrayListOf()

            var sentenceList: MutableList<Sentence> = arrayListOf()

            val paragraphList: MutableList<Paragraph> = arrayListOf()


            val resultIteratorParagraph = tess.resultIterator

            while (resultIteratorParagraph.next(TessBaseAPI.PageIteratorLevel.RIL_WORD)) {

                val word = Word()

                val rectParagraph = resultIteratorParagraph.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD)

                word.rect = rectParagraph.let { TextRest(it.left, it.top, it.right, it.bottom) }
                word.text = resultIteratorParagraph.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD)

                if ((word.rect!!.centerV() in sentence.rect!!.top..sentence.rect!!.bottom && word.rect!!.left - word.rect!!.height() < sentence.rect!!.right)) {

                    Log.d("tuanha", "executeTask: text:${word.text}")
                } else {
                    sentence = Sentence().apply {
                        rect = TextRest(0, 0, 0, 0)
                    }
                    Log.d("tuanha", "executeTask: text:${word.text} ${word.rect}")
                }

                sentence.rect!!.add(word.rect!!)

                paragraph.rect!!.add(word.rect!!)
//
//                val resultIteratorSentence = tess.resultIterator
//
//                val sentenceList: MutableList<Sentence> = arrayListOf()
//
//                while (resultIteratorSentence.next(TessBaseAPI.PageIteratorLevel.RIL_TEXTLINE)) {
//
//                    val sentence = Sentence()
//
//                    val rectSentence = resultIteratorSentence.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_TEXTLINE)
//
//                    sentence.rect = rectSentence.let { TextRest(it.left, it.top, it.right, it.bottom) }
//                    sentence.text = resultIteratorSentence.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_TEXTLINE)
//
//                    val resultIteratorWord = tess.resultIterator
//
//                    val wordList: MutableList<Word> = arrayListOf()
//
//                    while (resultIteratorWord.next(TessBaseAPI.PageIteratorLevel.RIL_WORD)) {
//
//                        val word = Word()
//
//                        val rectWord = resultIteratorWord.getBoundingRect(TessBaseAPI.PageIteratorLevel.RIL_WORD)
//
//                        word.rect = rectWord.let { TextRest(it.left, it.top, it.right, it.bottom) }
//                        word.text = resultIteratorWord.getUTF8Text(TessBaseAPI.PageIteratorLevel.RIL_WORD)
//
//                        Log.d("tuanha", "executeTask: ${word.text}")
//
//                        if (!rectSentence.contains(rectWord)) continue
//
//                        wordList.add(word)
//                    }
//
//                    sentence.words = wordList
//
//
//                    if (!rectParagraph.contains(rectSentence)) continue
//
//                    sentenceList.add(sentence)
//                }
//
//
//                paragraph.sentences = sentenceList
//
//                paragraphList.add(paragraph)
            }

            return paragraphList
        }

        data class Param(val bitmap: Bitmap)
    }
}