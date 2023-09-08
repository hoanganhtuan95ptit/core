package com.one.word.data.task.spelling.en

import com.one.state.ResultState
import com.one.crashlytics.logCrashlytics
import com.one.word.data.task.spelling.SpellingTask
import com.one.word.entities.Spelling
import org.jsoup.Jsoup

class EnSpellingTask : SpellingTask {

    override suspend fun execute(param: SpellingTask.Param): ResultState<List<Spelling>> {

        if (param.inputCode != "en") {

            return ResultState.Failed(RuntimeException("Not support"))
        }

        return runCatching {

            handleFromWiki(param.text)
        }.getOrElse {

            logCrashlytics(java.lang.RuntimeException("EnSpellingTask", it))

            ResultState.Failed(it)
        }
    }

    private fun handleFromWiki(text: String): ResultState<List<Spelling>> {

        val url = "https://en.wiktionary.org/wiki/"

        val document = Jsoup.connect("$url${text.lowercase()}").get()


        val langEntry = document.getElementsByClass("mw-parser-output").firstOrNull()?.children() ?: return ResultState.Failed(RuntimeException("not support"))


        val map = hashMapOf("us" to Spelling("us"), "uk" to Spelling("uk"))

        var record = false


        langEntry.forEach { element ->

            if (element.tagName() in listOf("h3", "h4")) {

                record = element.getElementsByClass("mw-headline").first().id() in listOf("Pronunciation")
            }

            if (!record) {
                return@forEach
            }

            if (element.tagName() in listOf("ul")) element.children().forEach {


                val text = it.text()

                val audio = it.getElementsByTag("source").firstOrNull()?.attr("src")?.takeIf { url -> url.isNotBlank() }
                    ?: it.getElementsByClass("mw-tmh-play").firstOrNull()?.attr("href")?.takeIf { url -> url.isNotBlank() }


                val spelling = it.getElementsByClass("IPA").firstOrNull()


                if (text.contains("us", true) && spelling != null) {
                    map["us"]?.text = spelling.text()
                }

                if (map["us"]?.text?.isBlank() == true && !text.contains("us", true) && !text.contains("uk", true) && spelling != null) {
                    map["us"]?.text = spelling.text()
                }

                if (text.contains("us", true) && audio != null) {
                    map["us"]?.audio = "https:$audio"
                }



                if (text.contains("uk", true) && spelling != null) {
                    map["uk"]?.text = spelling.text()
                }

                if (map["uk"]?.text?.isBlank() == true && !text.contains("us", true) && !text.contains("uk", true) && spelling != null) {
                    map["uk"]?.text = spelling.text()
                }

                if (text.contains("uk", true) && audio != null) {
                    map["uk"]?.audio = "https:$audio"
                }
            }
        }

        val list = map.values.filter {

            it.text.isNotBlank() && it.audio.isNotBlank()
        }.associateBy {

            it.code
        }.values.toList()


        return ResultState.Success(list)
    }

}