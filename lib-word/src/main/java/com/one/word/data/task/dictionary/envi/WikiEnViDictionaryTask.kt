package com.one.word.data.task.dictionary.envi

import com.one.state.ResultState
import com.one.state.toSuccess
import com.one.crashlytics.logCrashlytics
import com.one.word.data.task.dictionary.DictionaryTask
import com.one.word.entities.TextLevel
import com.one.word.entities.TextLevelType
import com.one.word.entities.TextLevelType.Companion.toTextLevelType
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class WikiEnViDictionaryTask : DictionaryTask {

    override suspend fun execute(param: DictionaryTask.Param): ResultState<List<TextLevel>> {

        if (param.inputCode != "en" || param.outputCode != "vi") {

            return ResultState.Failed(RuntimeException("Not support"))
        }

        return runCatching {

            handleFromWiki(param.text)
        }.getOrElse {

            logCrashlytics(java.lang.RuntimeException("WikiEnViDictionaryTask", it))

            ResultState.Failed(it)
        }
    }

    private fun handleFromWiki(text: String, lever: Int = 0): ResultState<List<TextLevel>> {

        val document = Jsoup.connect("https://vi.wiktionary.org/wiki/${text.lowercase()}#Ti%E1%BA%BFng_Anh").get()

        val sources = document.getElementsByClass("mw-parser-output").firstOrNull()?.children() ?: return ResultState.Failed(RuntimeException("Not found"))


        val englishH2 = document.getElementsByTag("h2").find { it.children().any { it.id() == "Tiếng_Anh" } }

        val start = if (englishH2 != null) {
            sources.indexOf(englishH2)
        } else {
            -1
        }


        var count = -1

        var record = false

        val elements = sources.mapNotNull { element ->

            count++

            if (count <= start) {
                return@mapNotNull null
            }

            if (element.tagName() in listOf("h3")) {

                record = !listOf("Tham_khảo", "Trái_nghĩa", "Đồng_nghĩa", "Từ_đồng_âm", "Từ_nguyên", "Cách_phát_âm", "Từ_tương_tự", "Từ_nguyên").any { element.getElementsByClass("mw-headline").first().id().contains(it, true) }
            }

            if (record && element.tagName() in listOf("h4")) {

                record = false
            }

            if (!record) {
                return@mapNotNull null
            }

            element
        }


        val list = arrayListOf<TextLevel>()


        elements.forEach { element ->

            list.addAll(element.detectH3(TextLevelType.H1))

            list.addAll(element.detectOl(TextLevelType.H2))
        }


        val mention = if (list.size <= 2 && lever == 0) {

            elements.mapNotNull { element -> element.getElementsByClass("mention").takeIf { it.isNotEmpty() } }.firstOrNull()
        } else {

            null
        }

        if (mention != null) handleFromWiki(mention.text(), lever + 1).toSuccess()?.data?.let {

            list.addAll(it)
        }

        return ResultState.Success(list)
    }

    private fun Element.detectH3(fromLevel: TextLevelType): List<TextLevel> {

        val list = arrayListOf<TextLevel>()

        if (tagName() !in listOf("h3")) {

            return list
        }

        TextLevel(getElementsByClass("mw-headline").first().html()).apply {

            level = fromLevel
        }.let {

            list.add(it)
        }

        return list
    }

    private fun Element.detectOl(fromLevel: TextLevelType): List<TextLevel> {

        val list = arrayListOf<TextLevel>()

        if (tagName() !in listOf("ol")) {

            return list
        }

        children().forEachIndexed { index, element ->

            list.addAll(element.detectLi(fromLevel, index))
        }

        return list
    }

    private fun Element.detectLi(fromLevel: TextLevelType, index: Int): List<TextLevel> {

        val list = arrayListOf<TextLevel>()

        if (tagName() !in listOf("li")) {

            return list
        }

        val children = children()

        children.forEach {

            list.addAll(it.detectDl((fromLevel.value + 1).toTextLevelType()))
        }


        select("dl").remove()


        list.add(0, TextLevel("${index + 1}. ${html()}").apply { level = fromLevel })

        return list
    }

    private fun Element.detectDl(fromLevel: TextLevelType): List<TextLevel> {

        val list = arrayListOf<TextLevel>()

        if (tagName() !in listOf("dl")) {

            return list
        }

        children().mapIndexed { index, element ->

            TextLevel(element.html()).apply { level = fromLevel }
        }.let {

            list.addAll(it)
        }

        return list
    }
}