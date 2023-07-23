package com.one.coreapp.ui.base.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.one.coreapp.utils.extentions.Enable
import com.one.crashlytics.logCrashlytics
import com.one.coreapp.utils.extentions.postDifferentValue
import com.one.coreapp.utils.extentions.toEnable
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), KoinComponent {

    open val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
        logCrashlytics(throwable)
    }

    val uiReady: LiveData<Boolean> = MediatorLiveData()

    val uiVisibleEnable: LiveData<Enable> = uiReady.toEnable()

    fun updateUiReady(uiReady: Boolean) {
        this.uiReady.postDifferentValue(uiReady)
    }
}
