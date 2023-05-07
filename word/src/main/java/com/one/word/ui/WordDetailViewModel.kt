package com.one.word.ui

import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.webkit.URLUtil
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.one.adapter.LoadingViewItem
import com.one.adapter.ViewItemCloneable
import com.one.coreapp.data.usecase.ResultState
import com.one.coreapp.data.usecase.doSuccess
import com.one.coreapp.data.usecase.isStart
import com.one.coreapp.data.usecase.toSuccess
import com.one.coreapp.ui.adapters.SpaceViewItem
import com.one.coreapp.ui.base.viewmodels.BaseViewModel
import com.one.coreapp.utils.extentions.*
import com.one.translate.data.usecase.TranslateUseCase
import com.one.word.R
import com.one.word.data.usecase.FetchWordDictionaryUseCase
import com.one.word.data.usecase.FetchWordSpellingUseCase
import com.one.word.entities.TextLevelType
import com.one.word.ui.adapters.SpellingViewItem
import com.one.word.ui.adapters.TextViewItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*


class WordDetailViewModel(
    private val text: String,
    private val inputCode: String,
    private val translateUseCase: TranslateUseCase,
    private val fetchWordSpellingUseCase: FetchWordSpellingUseCase,
    private val fetchWordDictionaryUseCase: FetchWordDictionaryUseCase,
) : BaseViewModel() {

    private val itemLoading = listOf(
        LoadingViewItem(R.layout.item_text_loading_1),
        LoadingViewItem(R.layout.item_text_loading_2),
        LoadingViewItem(R.layout.item_text_loading_1)
    )

    val isLink: LiveData<Boolean> = liveData {

        val validate = URLUtil.isValidUrl(text)

        postDifferentValue(validate)
    }

    val isNumberPhone: LiveData<Boolean> = liveData {

        if (TextUtils.isEmpty(text)) {

            postDifferentValue(false)
            return@liveData
        }


        val validate = try {

            val phoneNumberUtil = PhoneNumberUtil.getInstance()
            val phoneNumber = phoneNumberUtil.parse(text, Locale.getDefault().country)
            val phoneNumberType = phoneNumberUtil.getNumberType(phoneNumber)
            phoneNumberType == PhoneNumberType.MOBILE
        } catch (e: Exception) {

            false
        }

        postDifferentValue(validate)
    }


    @VisibleForTesting
    val targetLanguage: LiveData<String> = MediatorLiveData()

    @VisibleForTesting
    val wordDetailState: LiveData<ResultState<List<ViewItemCloneable>>> = liveData<ResultState<List<ViewItemCloneable>>> {

        postDifferentValue(ResultState.Start)

        val inputCode = this@WordDetailViewModel.inputCode.takeIf { it.isNotBlank() } ?: identifyLanguage(text)


        val spellingAsync = viewModelScope.async(handler + Dispatchers.IO) {
            fetchWordSpellingUseCase.execute(FetchWordSpellingUseCase.Param(text, inputCode)).toSuccess()?.data
        }

        val translateAsync = viewModelScope.async(handler + Dispatchers.IO) {
            translateUseCase.execute(TranslateUseCase.Param(listOf(text), inputCode, Locale.getDefault().language)).toSuccess()?.data?.firstOrNull()
        }

        val dictionaryAsync = viewModelScope.async(handler + Dispatchers.IO) {
            fetchWordDictionaryUseCase.execute(FetchWordDictionaryUseCase.Param(text, inputCode, Locale.getDefault().language)).toSuccess()?.data
        }

        val list = arrayListOf<ViewItemCloneable>()

        spellingAsync.await()?.map {

            SpellingViewItem(it).refresh()
        }?.let {

            list.addAll(it)
            list.add(SpaceViewItem(16.toPx()))
        }


        dictionaryAsync.await()?.map {

            val textSize = if (it.level == TextLevelType.H1) 18f else 14f

            val textTypeface = if (it.level == TextLevelType.H1) Typeface.BOLD else Typeface.NORMAL

            val paddingLeft = it.level.value * 24.toPx()

            TextViewItem(text = TextHtml(it.text), textSize = textSize, textTypeface = textTypeface, paddingLeft = paddingLeft)
        }?.let {

            list.addAll(it)
        } ?: translateAsync.await()?.let {

            TextViewItem(TextStr(it))
        }?.let {

            list.add(it)
        }

        postValue(ResultState.Success(list))
    }.apply {

        postDifferentValue(ResultState.Start)
    }

    @VisibleForTesting
    val viewItemList: LiveData<List<ViewItemCloneable>> = combineSources(wordDetailState) {

        val state = wordDetailState.get()

        if (state.isStart()) {

            postValue(itemLoading)
            return@combineSources
        }

        state.doSuccess {

            postValue(it)
        }
    }

    val viewItemListDisplay: LiveData<List<ViewItemCloneable>> = combineSources(viewItemList) {

        viewItemList.getOrEmpty().map {
            it.clone()
        }.let {
            postValue(it)
        }
    }


    val webVisible: LiveData<Boolean> = combineSources(viewItemListDisplay) {

        if (value == null && viewItemListDisplay.get().any { it is TextViewItem || it is SpellingViewItem }) {

            postDifferentValue(false)
        }
    }

    val webLoadData: LiveData<String> = combineSources(viewItemListDisplay, webVisible) {

        if (!webVisible.get() || !viewItemListDisplay.get().any { it is TextViewItem || it is SpellingViewItem }) {
            return@combineSources
        }

        postDifferentValue("https://www.google.com/search?q=${text}&tbm=isch")
    }

    fun updateWebVisible(webVisible: Boolean) {
        this.webVisible.postDifferentValue(webVisible)
    }

    fun updateTargetLanguage(targetLanguage: String) {
        this.targetLanguage.postDifferentValue(targetLanguage)
    }

    private suspend fun identifyLanguage(text: String) = suspendCancellableCoroutine<String> { a ->

        LanguageIdentification.getClient().identifyLanguage(text).addOnSuccessListener { languageCode ->

            a.resumeActive(languageCode.takeIf { !it.equals("und", true) } ?: "")
        }.addOnFailureListener {

            a.resumeActive("")
        }
    }
}