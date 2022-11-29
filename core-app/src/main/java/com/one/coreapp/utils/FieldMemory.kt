package com.one.coreapp.utils

import android.os.Looper
import com.one.coreapp.utils.extentions.offerActive
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import java.util.concurrent.CopyOnWriteArrayList


class FieldMemory<T>(private var _data: T? = null) {

    private val notifications: CopyOnWriteArrayList<FieldMemoryNotification> = CopyOnWriteArrayList()

    fun setData(data: T?) {

        if (this._data == data) return

        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            error("function need call in io thread")
        }

        this._data = data

        notifications.forEach {
            it.notification()
        }
    }

    fun getData(): T? {

        return this._data
    }

    fun getDataAsync() = channelFlow<T> {

        val notification = object : FieldMemoryNotification {

            override fun notification() {
                getData()?.let { it1 -> offerActive(it1) }
            }
        }

        notification.notification()

        notifications.add(notification)

        awaitClose {

            notifications.remove(notification)
        }
    }

    private interface FieldMemoryNotification {
        fun notification()
    }
}