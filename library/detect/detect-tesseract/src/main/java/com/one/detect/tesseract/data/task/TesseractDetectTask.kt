package com.one.detect.tesseract.data.task

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.simple.core.utils.extentions.resumeActive
import com.simple.core.utils.extentions.toArrayList
import com.simple.detect.data.tasks.DetectTask
import com.simple.detect.entities.Paragraph
import com.simple.image.toBitmap
import com.simple.state.ResultState
import com.simple.state.isFailed
import com.simple.state.toFailed
import com.simple.state.toSuccess
import com.simple.task.LowException
import com.simple.task.executeAsyncAll
import downloadSyncV2
import getFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine


class TesseractDetectTask(
    private val context: Context
) : DetectTask {

    private val languageMap = hashMapOf(
        "af" to listOf("afr"),
        "am" to listOf("amh"),
        "ar" to listOf("ara"),
        "as" to listOf("asm"),
        "az" to listOf("aze"),
//        "" to listOf("aze_cyrl"),
        "be" to listOf("bel"),
        "bn" to listOf("ben"),
        "bo" to listOf("bod"),
        "bs" to listOf("bos"),
        "br" to listOf("bre"),
        "bg" to listOf("bul"),
        "ca" to listOf("cat"),
//        "" to listOf("ceb"),
        "cs" to listOf("ces"),
        "zh" to listOf("chi_sim"),
//        "" to listOf("chi_tra"),
//        "" to listOf("chr"),
        "co" to listOf("cos"),
        "cy" to listOf("cym"),
        "da" to listOf("dan"),
        "de" to listOf("deu"),
        "dv" to listOf("div"),
        "dz" to listOf("dzo"),
        "el" to listOf("ell"),
        "en" to listOf("eng"),
//        "" to listOf("enm"),
        "eo" to listOf("epo"),
//        "" to listOf("equ"),
        "et" to listOf("est"),
        "eu" to listOf("eus"),
        "fa" to listOf("fas"),
        "fo" to listOf("fao"),
//        "" to listOf("fil"),
        "fi" to listOf("fin"),
        "fr" to listOf("fra"),
//        "" to listOf("frk"),
//        "" to listOf("frm"),
        "fy" to listOf("fry"),
        "gd" to listOf("gla"),
        "ga" to listOf("gle"),
        "gl" to listOf("glg"),
//        "" to listOf("grc"),
        "gu" to listOf("guj"),
        "ht" to listOf("hat"),
        "he" to listOf("heb"),
        "hi" to listOf("hin"),
        "hr" to listOf("hrv"),
        "hu" to listOf("hun"),
        "hy" to listOf("hye"),
        "iu" to listOf("iku"),
        "id" to listOf("ind"),
        "is" to listOf("isl"),
        "it" to listOf("ita"),
//        "" to listOf("ita_old"),
        "ja" to listOf("jav"),
        "ja" to listOf("jpn"),
        "kn" to listOf("kan"),
        "ka" to listOf("kat"),
//        "" to listOf("kat_old"),
        "kk" to listOf("kaz"),
        "km" to listOf("khm"),
        "ky" to listOf("kir"),
//        "" to listOf("kmr"),
        "ko" to listOf("kor"),
//        "" to listOf("kor_vert"),
        "lo" to listOf("lao"),
        "la" to listOf("lat"),
        "lv" to listOf("lav"),
        "lt" to listOf("lit"),
        "lb" to listOf("ltz"),
        "ml" to listOf("mal"),
        "mr" to listOf("mar"),
        "mk" to listOf("mkd"),
        "mt" to listOf("mlt"),
        "mn" to listOf("mon"),
        "mi" to listOf("mri"),
        "ms" to listOf("msa"),
        "my" to listOf("mya"),
        "ne" to listOf("nep"),
        "nl" to listOf("nld"),
        "no" to listOf("nor"),
        "oc" to listOf("oci"),
        "or" to listOf("ori"),
//        "" to listOf("osd"),
        "pa" to listOf("pan"),
        "pl" to listOf("pol"),
        "pt" to listOf("por"),
        "ps" to listOf("pus"),
        "qu" to listOf("que"),
        "ro" to listOf("ron"),
        "ru" to listOf("rus"),
        "sa" to listOf("san"),
        "si" to listOf("sin"),
        "sk" to listOf("slk"),
        "sl" to listOf("slv"),
        "sd" to listOf("snd"),
        "es" to listOf("spa"),
//        "" to listOf("spa_old"),
        "sq" to listOf("sqi"),
        "sr" to listOf("srp"),
//        "" to listOf("srp_latn"),
        "su" to listOf("sun"),
        "sw" to listOf("swa"),
//        "" to listOf("swe"),
//        "" to listOf("syr"),
        "ta" to listOf("tam"),
        "tt" to listOf("tat"),
        "te" to listOf("tel"),
        "tg" to listOf("tgk"),
        "th" to listOf("tha"),
        "ti" to listOf("tir"),
        "to" to listOf("ton"),
        "tr" to listOf("tur"),
        "ug" to listOf("uig"),
        "uk" to listOf("ukr"),
        "ur" to listOf("urd"),
        "uz" to listOf("uzb"),
//        "" to listOf("uzb_cyrl"),
        "vi" to listOf("vie"),
        "yi" to listOf("yid"),
        "yo" to listOf("yor"),
    )

    override suspend fun executeTask(param: DetectTask.Param): List<Paragraph> {


        val child = "tessdata"

        val fileRoot = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!

        val languageList = languageMap[param.inputCode] ?: throw LowException("not found language code from ${param.inputCode}")

        languageList.map { language ->

            CoroutineScope(Dispatchers.IO).async {

                val file = "$child/$language.traineddata".getFile(fileRoot, false)

                if (file.exists()) return@async

                "$child/$language.traineddata".getFile(fileRoot, true).downloadSyncV2("https://raw.githubusercontent.com/tesseract-ocr/tessdata_best/main/${language}.traineddata")
            }
        }.awaitAll()


        val bitmap = (param.source as? Bitmap)
            ?: (param.source as? String)?.toBitmap(context = context, width = param.sizeMax, height = param.sizeMax)
            ?: throw LowException("not support ${param.source.javaClass.simpleName}")


        val states = languageList.map { language ->

            fileRoot to language
        }.map {

            TesseractTask(fileRoot, it.second)
        }.executeAsyncAll(TesseractTask.Param(bitmap)).first().toSuccess()?.data ?: emptyList()


        if (states.all { it.isFailed() }) {

            throw states.filterIsInstance<ResultState.Failed>().minByOrNull { if (it.toFailed()?.cause !is LowException) 0 else 1 }!!.cause
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

    private suspend fun identifyLanguage(text: String) = suspendCancellableCoroutine { a ->

        LanguageIdentification.getClient(LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.34f).build()).identifyLanguage(text).addOnSuccessListener { languageCode ->

            a.resumeActive(languageCode.lowercase().replace("-latn", "").replace("und", ""))
        }.addOnFailureListener {

            a.resumeActive("")
        }
    }
}