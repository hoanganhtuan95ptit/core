package com.one.word

import android.app.Application
import com.one.coreapp.Module
import com.one.word.data.task.dictionary.DictionaryTask
import com.one.word.data.task.dictionary.envi.WikiEnViDictionaryTask
import com.one.word.data.task.spelling.SpellingTask
import com.one.word.data.task.spelling.en.EnSpellingTask
import com.one.word.data.usecase.FetchWordDictionaryUseCase
import com.one.word.data.usecase.FetchWordSpellingUseCase
import com.one.word.ui.WordDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module


private val module = module {


    single { EnSpellingTask() } bind SpellingTask::class


    single { WikiEnViDictionaryTask() } bind DictionaryTask::class


    single {
        FetchWordSpellingUseCase(getAll())
    }

    single {
        FetchWordDictionaryUseCase(getAll())
    }


    viewModel { (text: String, inputCode: String) ->
        WordDetailViewModel(text, inputCode, get(), get(), get())
    }
}

private val loadKoinModules by lazy {

    loadKoinModules(
        listOf(
            module,
        )
    )
}


private fun injectModule() = loadKoinModules


class WordModule : Module {

    override fun init(application: Application) {
        injectModule()
    }
}