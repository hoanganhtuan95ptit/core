package com.one.coreapp.utils.extentions

import android.annotation.SuppressLint
import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.one.core.entities.Comparable
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.resume

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

@SuppressLint("WrongThread")
@AnyThread
suspend fun <T : Comparable> LiveData<T>.postDifferentValueIfActive(t: T): Boolean {

    return if (isActive()) postDifferentValue(t)
    else false
}

@SuppressLint("WrongThread")
@AnyThread
suspend fun <T : Comparable> LiveData<List<T>>.postDifferentValueIfActive(t: List<T>): Boolean {

    return if (isActive()) postDifferentValue(t)
    else false
}

@SuppressLint("WrongThread")
@AnyThread
suspend fun <T> LiveData<T>.postDifferentValueIfActive(t: T, checkSame: (old: T?, new: T) -> Boolean = { old, new -> old == new }): Boolean {

    return if (isActive()) postDifferentValue(t, checkSame)
    else false
}

@SuppressLint("WrongThread")
@AnyThread
fun <T : Comparable> LiveData<T>.postDifferentValue(t: T) = postDifferentValue(t) { old, new ->

    old?.getListCompare() == new.getListCompare()
}

@SuppressLint("WrongThread")
@AnyThread
fun <T : Comparable> LiveData<List<T>>.postDifferentValue(t: List<T>) = postDifferentValue(t) { old, new ->

    old?.flatMap { item -> item.getListCompare() } == new.flatMap { item -> item.getListCompare() }
}

@SuppressLint("WrongThread")
@AnyThread
fun <T> LiveData<T>.postDifferentValue(t: T, checkSame: (old: T?, new: T) -> Boolean = { old, new -> old == new }): Boolean {

    if (checkSame.invoke(this.value, t)) {
        return false
    }

    if (this is MutableLiveData && Thread.currentThread().name.contains("main", true)) {
        value = t
    } else {
        postValue(t)
    }

    return true
}

@AnyThread
fun <T> LiveData<T>?.postDifferentValue(t: T) {

    if (this?.value == t) {
        return
    }

    postValue(t)
}

@AnyThread
fun <T> LiveData<T>?.postValue(t: T?) {
    when (this) {
        is MediatorLiveData<T> -> this.postValue(t)
        is MutableLiveData<T> -> this.postValue(t)
        else -> error("Not support ${this?.javaClass?.simpleName}")
    }
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

        override fun onChanged(o: T) {
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


inline fun <T> LiveData<T>.doDifferent(action: MediatorLiveData<T>.() -> Unit) {

    if (this !is CacheLiveData) {
        error("not support ${this.javaClass.simpleName}")
    }

    if (isDifferentSourceListener) {
        action(this)
    }
}

open class CacheLiveData<T> : MediatorLiveData<T>() {

    var isDifferentSourceListener: Boolean = false
}
