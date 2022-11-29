package com.one.coreapp.utils.extentions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class ResetLiveData<T>(private val default: T) : MediatorLiveData<T>() {

    override fun onInactive() {
        super.onInactive()
        value = default
    }
}

fun <T> LiveData<T>.toResetLiveData(default: T) = ResetLiveData(default).apply {
    addSource(this@toResetLiveData) {
        setValue(it)
    }
}
