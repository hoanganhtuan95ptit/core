package com.one.detect.tesseract.data.task

import android.graphics.Bitmap
import android.util.Log
import com.googlecode.tesseract.android.ResultIterator
import com.googlecode.tesseract.android.TessBaseAPI
import com.simple.detect.entities.Paragraph
import com.simple.detect.entities.Sentence
import com.simple.detect.entities.TextBlock
import com.simple.detect.entities.TextRest
import com.simple.detect.entities.Word
import com.simple.detect.entities.add
import com.simple.detect.entities.contains
import com.simple.task.LowException
import com.simple.task.Task
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.coroutineContext

class TesseractTask(private val fileRoot: File, private val languageCode: String) : Task<TesseractTask.Param, List<Paragraph>> {

    override suspend fun executeTask(param: Param): List<Paragraph> = withContext(coroutineContext) {


        val tess = TessBaseAPI()


        if (!tess.init(fileRoot.absolutePath, languageCode, TessBaseAPI.OEM_LSTM_ONLY)) {

            throw LowException("not init ${fileRoot.absolutePath} - $languageCode")
        }


        tess.pageSegMode = TessBaseAPI.PageSegMode.PSM_AUTO_ONLY
        tess.setImage(param.bitmap)

        val iterator = tess.resultIterator

        val wordList: MutableList<Word> = iterator.read()

        val sentenceList: MutableList<Sentence> = iterator.read()

        val paragraphList: MutableList<Paragraph> = iterator.read()

        tess.recycle()


        var wordIndex = 0
        val sentenceListNew: MutableList<Sentence> = arrayListOf()

        for (j in (if (sentenceList.isEmpty()) -1 else 0) until sentenceList.size) {

            val sentence = sentenceList.getOrNull(j)?.also {
                sentenceListNew.add(it)
            }
            var hasWordContain = false

            for (i in wordIndex..wordList.size) {

                val word = wordList.getOrNull(i) ?: break

                wordIndex = i

                if (sentence?.rect?.contains(word.rect!!) == true) {

                    hasWordContain = true
                    (sentence.words as ArrayList).add(word)
                } else if (!hasWordContain) Sentence().apply {

                    rect = word.rect
                    text = word.text
                }.also {

                    sentenceListNew.add(it)
                } else {

                    break
                }
            }
        }


        var sentenceIndex = 0
        val paragraphListNew: MutableList<Paragraph> = arrayListOf()

        for (j in (if (paragraphList.isEmpty()) -1 else 0) until paragraphList.size) {

            val paragraph = paragraphList.getOrNull(j)?.also {
                paragraphListNew.add(it)
            }
            var hasSentenceContain = false

            for (i in sentenceIndex..sentenceListNew.size) {

                val sentence = sentenceListNew.getOrNull(i) ?: break

                sentenceIndex = i

                if (paragraph?.rect?.contains(sentence.rect!!) == true) {

                    hasSentenceContain = true
                    (paragraph.sentences as ArrayList).add(sentence)
                } else if (!hasSentenceContain) Paragraph().apply {

                    rect = sentence.rect
                    text = sentence.text
                }.also {

                    paragraphListNew.add(it)
                } else {

                    break
                }
            }
        }


        paragraphListNew.map { paragraph ->

            async {

                var sentence: Sentence? = null

                val sentences: MutableList<Sentence> = arrayListOf()

                val list = paragraph.sentences.flatMap { it.words }

                list.forEachIndexed { index, word ->

                    if (list.getOrNull(index - 1)?.text?.trim()?.endsWith(".", true) != false) sentence = Sentence().apply {

                        rect = TextRest(Int.MAX_VALUE, Int.MAX_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
                        words = arrayListOf()
                    }.also {

                        sentences.add(it)
                    }

                    val rect = word.rect ?: return@forEachIndexed

                    (sentence?.words as? ArrayList)?.add(word)

                    sentence?.rect?.add(rect)
                    sentence?.text = sentence?.words?.joinToString(" ") { it.text } ?: ""
                }

                paragraph.text = sentences.joinToString(" ") { it.text }
                paragraph.sentences = sentences
            }
        }.awaitAll()

        return@withContext paragraphListNew
    }


    inline fun <reified T : TextBlock> ResultIterator.read(): MutableList<T> {

        begin()

        val list: MutableList<T> = arrayListOf()

        val level = if (T::class == Paragraph::class) {
            TessBaseAPI.PageIteratorLevel.RIL_PARA
        } else if (T::class == Sentence::class) {
            TessBaseAPI.PageIteratorLevel.RIL_TEXTLINE
        } else if (T::class == Word::class) {
            TessBaseAPI.PageIteratorLevel.RIL_WORD
        } else {
            return arrayListOf()
        }


        do {

            val text: T = if (T::class == Paragraph::class) {
                Paragraph().apply { sentences = arrayListOf() } as T
            } else if (T::class == Sentence::class) {
                Sentence().apply { words = arrayListOf() } as T
            } else if (T::class == Word::class) {
                Word() as T
            } else {
                return arrayListOf()
            }

            text.rect = getBoundingRect(level).let { TextRest(it.left, it.top, it.right, it.bottom) }
            text.text = kotlin.runCatching { getUTF8Text(level) }.getOrNull() ?: continue

            list.add(text)

        } while (next(level))


        return list
    }

    data class Param(val bitmap: Bitmap)
}