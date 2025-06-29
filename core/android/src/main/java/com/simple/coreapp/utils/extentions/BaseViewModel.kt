package com.simple.coreapp.utils.extentions

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModels.BaseViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.simple.coreapp.utils.ext.handler
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@MainThread
fun <T> BaseViewModel.mediatorLiveData(context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onChanged: suspend MediatorLiveData<T>.() -> Unit): LiveData<T> {

    val liveData = MediatorLiveData<T>()

    viewModelScope.launch(context = context ?: (handler + Dispatchers.IO), start = start) {
        onChanged.invoke(liveData)
    }

    return liveData
}

@MainThread
fun <T> BaseViewModel.combineSources(vararg sources: LiveData<*>, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit): LiveData<T> {

    var job: Job? = null

    val liveData = CacheLiveData<T>()

    val change: (isDifferentSourceListener: Boolean) -> Unit = {

        job?.cancel()

        job = viewModelScope.launch(context = context ?: (handler + Dispatchers.IO), start = start) {
            liveData.isDifferentSourceListener = it
            onChanged.invoke(liveData, sources.toList())
        }
    }

    var valuesOld: List<Any?> = emptyList()

    sources.forEach {

        liveData.addSource(it) {

            val values = sources.map { source -> source.value }

            val enableValues = values.filterIsInstance<Enable>()

            if (enableValues.isNotEmpty() && enableValues.any { enable -> !enable.enable }) {

                job?.cancel()
            } else if (values.all { value -> value != null }) {

                change.invoke(valuesOld != values)
                valuesOld = values
            }
        }
    }

    return liveData
}

@MainThread
fun <T> BaseViewModel.listenerSources(vararg sources: LiveData<*>, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit): LiveData<T> {

    var job: Job? = null

    val liveData = CacheLiveData<T>()

    val change: (isDifferentSourceListener: Boolean) -> Unit = {

        job?.cancel()

        job = viewModelScope.launch(context = context ?: (handler + Dispatchers.IO), start = start) {
            liveData.isDifferentSourceListener = it
            onChanged.invoke(liveData, sources.toList())
        }
    }

    var valuesOld: List<Any?> = emptyList()

    sources.forEach {

        liveData.addSource(it) {

            val values = sources.map { source -> source.value }

            val enableValues = values.filterIsInstance<Enable>()

            if (enableValues.isNotEmpty() && enableValues.any { enable -> !enable.enable }) {

                job?.cancel()
            } else {

                change.invoke(valuesOld != values)
                valuesOld = values
            }
        }
    }

    return liveData
}

@MainThread
fun <T> BaseViewModel.mediatorLiveDataWithDiff(
    context: CoroutineContext? = null,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onChanged: suspend MediatorLiveData<T>.() -> Unit
): LiveData<T> = mediatorLiveData(context, start, onChanged).distinctUntilChanged()

@MainThread
fun <T> BaseViewModel.combineSourcesWithDiff(
    vararg sources: LiveData<*>,
    context: CoroutineContext? = null,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit
): LiveData<T> = combineSources(sources = sources, context, start, onChanged).distinctUntilChanged()

@MainThread
fun <T> BaseViewModel.listenerSourcesWithDiff(
    vararg sources: LiveData<*>,
    context: CoroutineContext? = null,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit
): LiveData<T> = listenerSources(sources = sources, context, start, onChanged).distinctUntilChanged()