package com.one.coreapp.utils.extentions

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.one.coreapp.ui.base.viewmodels.BaseViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@MainThread
fun <T> BaseViewModel.liveData(context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onChanged: suspend MediatorLiveData<T>.() -> Unit): LiveData<T> {

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
fun <T> BaseViewModel.combineSources(source: () -> List<LiveData<*>>, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit): LiveData<T> {

    var job: Job? = null

    return object : CacheLiveData<T>() {

        var addSource: Boolean = false

        override fun onActive() {
            super.onActive()

            if (addSource) return
            addSource = true

            val sources = source.invoke().toList()

            val self = this

            val change: (isDifferentSourceListener: Boolean) -> Unit = {

                job?.cancel()

                job = viewModelScope.launch(context = context ?: (handler + Dispatchers.IO), start = start) {
                    self.isDifferentSourceListener = it
                    onChanged.invoke(self, sources)
                }
            }

            var valuesOld: List<Any?> = emptyList()

            sources.forEach {

                self.addSource(it) {

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
        }
    }
}


@MainThread
fun <T> BaseViewModel.listenerSources(source: () -> List<LiveData<*>>, context: CoroutineContext? = null, start: CoroutineStart = CoroutineStart.DEFAULT, onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit): LiveData<T> {

    var job: Job? = null

    return object : CacheLiveData<T>() {

        var addSource: Boolean = false

        override fun onActive() {
            super.onActive()

            if (addSource) return
            addSource = true

            val sources = source.invoke().toList()

            val self = this

            val change: (isDifferentSourceListener: Boolean) -> Unit = {

                job?.cancel()

                job = viewModelScope.launch(context = context ?: (handler + Dispatchers.IO), start = start) {
                    self.isDifferentSourceListener = it
                    onChanged.invoke(self, sources)
                }
            }

            var valuesOld: List<Any?> = emptyList()

            sources.forEach {

                self.addSource(it) {

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
        }
    }
}