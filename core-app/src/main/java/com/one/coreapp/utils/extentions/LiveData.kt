package com.one.coreapp.utils.extentions

import android.annotation.SuppressLint
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.one.adapter.ViewItem
import com.one.coreapp.App
import com.one.coreapp.ui.base.viewmodels.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.resume

@MainThread
fun <T> BaseViewModel.liveData(onChanged: suspend MediatorLiveData<T>.() -> Unit): LiveData<T> {

    val liveData = MediatorLiveData<T>()

    viewModelScope.launch(handler + App.shared.dispatcherForHandleDataUi) {
        onChanged.invoke(liveData)
    }

    return liveData
}

@MainThread
fun <T> BaseViewModel.combineSources(vararg sources: LiveData<*>, onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit): LiveData<T> {

    var job: Job? = null

    val liveData = MediatorLiveData<T>()

    val change: () -> Unit = {

        job?.cancel()

        job = viewModelScope.launch(handler + App.shared.dispatcherForHandleDataUi) {
            onChanged.invoke(liveData, sources.toList())
        }
    }

    sources.forEach {
        liveData.addSource(it) {
            if (sources.mapNotNull { source -> source.value }.filterIsInstance<Enable>().any { enable -> !enable.enable }) liveData.postDifferentValue(null)
            else if (sources.all { source -> source.value != null }) change.invoke()
        }
    }

    return liveData
}

@MainThread
fun <T> BaseViewModel.listenerSources(vararg sources: LiveData<*>, onChanged: suspend MediatorLiveData<T>.(List<LiveData<*>>) -> Unit): LiveData<T> {

    var job: Job? = null

    val liveData = MediatorLiveData<T>()

    val change: () -> Unit = {

        job?.cancel()

        job = viewModelScope.launch(handler + App.shared.dispatcherForHandleDataUi) {
            onChanged.invoke(liveData, sources.toList())
        }
    }

    sources.forEach {
        liveData.addSource(it) {
            change.invoke()
        }
    }

    return liveData
}

@MainThread
@SuppressLint("NullSafeMutableLiveData")
fun LiveData<Boolean>.toEnable(): LiveData<Enable> {

    val liveData = MediatorLiveData<Enable>()

    val change: (Boolean) -> Unit = {

        liveData.postDifferentValue(Enable(it))
    }

    liveData.addSource(this) {
        change.invoke(it)
    }

    return liveData
}

@MainThread
@SuppressLint("NullSafeMutableLiveData")
fun <X> LiveData<X>.toEvent(): LiveData<Event<X>> = map {
    Event(it)
}

@AnyThread
fun <T> LiveData<T>?.postValue(t: T?) {
    when (this) {
        is MediatorLiveData<T> -> this.postValue(t)
        is MutableLiveData<T> -> this.postValue(t)
        else -> error("Not support ${this?.javaClass?.simpleName}")
    }
}

@AnyThread
fun <T> LiveData<T>?.postDifferentValue(t: T) {

    if (this?.value == t) {
        return
    }

    postValue(t)
}


@AnyThread
fun <T : ViewItem> LiveData<List<T>>.postDifferentValue(t: List<T>) = postDifferentValue(t) { old, new ->

    old?.flatMap { item -> item.getContentsCompare().map { pair -> pair.first } } == new.flatMap { item -> item.getContentsCompare().map { pair -> pair.first } }
}


@AnyThread
fun <T> LiveData<T>.postDifferentValue(t: T, checkSame: (old: T?, new: T) -> Boolean = { old, new -> old == new }) {

    if (checkSame.invoke(this.value, t)) {
        return
    }

    postValue(t)
}


fun <Y, X> LiveData<Map<Y, X>>.getOrEmpty(): Map<Y, X> {
    return getOrDefault(emptyMap())
}

fun <X> LiveData<List<X>>.getOrEmpty(): List<X> {
    return getOrDefault(emptyList())
}

fun LiveData<String>.getOrEmpty(): String {
    return value ?: ""
}

fun <X> LiveData<X>.getOrDefault(default: X): X {
    return value ?: default
}

fun <X> LiveData<X>.getOrNull(): X? {
    return value
}

fun <X> LiveData<X>.get(): X {
    return value!!
}


fun <X> X.toEvent() = Event(this)

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

class Enable(val enable: Boolean) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Enable) return false

        if (enable != other.enable) return false

        return true
    }

    override fun hashCode(): Int {
        return enable.hashCode()
    }
}

suspend fun <T> LiveData<T>.getOrAwaitCause(caseStop: (T?) -> Boolean = { true }) = suspendCancellableCoroutine<T?> {

    var data: T?

    val latch = CountDownLatch(1)

    val observer = object : Observer<T> {

        override fun onChanged(o: T?) {
            data = o

            if (caseStop.invoke(o)) {

                latch.countDown()

                if (!it.isCompleted) it.resume(data)

                this@getOrAwaitCause.removeObserver(this)
            }
        }
    }

    this.observeForever(observer)

    it.invokeOnCancellation {
        this@getOrAwaitCause.removeObserver(observer)
    }
}
