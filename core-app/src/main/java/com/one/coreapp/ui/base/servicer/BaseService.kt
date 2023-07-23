package com.one.coreapp.ui.base.servicer


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.switchMap
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.one.coreapp.BaseApp
import com.one.coreapp.utils.extentions.CloseableCoroutineScope
import com.one.crashlytics.logCrashlytics
import kotlinx.coroutines.*
import java.io.Closeable
import java.io.IOException
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.CoroutineContext

abstract class BaseService : LifecycleService() {

    companion object {

        private const val JOB_KEY = "androidx.lifecycle.ViewModelCoroutineScope.JOB_KEY"

    }

    private var mCleared = false

    private val mBagOfTags = HashMap<String, Any>()

    private val listBroadcastReceiver = CopyOnWriteArrayList<BroadcastReceiver>()

    val tags: MutableMap<String, Any> = HashMap()

    val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
        logCrashlytics(throwable)
    }

    protected open fun registerReceiver(vararg action: String, receiver: (Intent) -> Unit) {
        registerReceiver(receiver, *action)
    }

    protected open fun registerReceiver(receiver: (Intent) -> Unit, vararg action: String) {
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let { receiver.invoke(it) }
            }
        }, *action)
    }

    protected open fun registerReceiver(receiver: BroadcastReceiver, vararg action: String) {
        listBroadcastReceiver.add(receiver)
        for (s in action) {
            LocalBroadcastManager.getInstance(BaseApp.shared).registerReceiver(receiver, IntentFilter(s))
        }
    }

    fun sendBroadcast(action: String, block: Intent.() -> Unit) {
        val intent = Intent(action).apply { block(this) }
        LocalBroadcastManager.getInstance(BaseApp.shared).sendBroadcast(intent)
    }

    override fun onDestroy() {
        mCleared = true

        for (broadcastReceiver in listBroadcastReceiver) {
            LocalBroadcastManager.getInstance(BaseApp.shared).unregisterReceiver(broadcastReceiver)
        }

        synchronized(mBagOfTags) {
            for (value in mBagOfTags.values) {
                closeWithRuntimeException(value)
            }
        }

        super.onDestroy()
    }

    protected fun getDispatcher(tag: String): CoroutineDispatcher {
        return getOrCreateAny("DISPATCHER-$tag") {
            newFixedThreadPoolContext(1, tag)
        }
    }

    inline fun <reified T : Any> getOrCreateAny(tag: String, block: () -> T): T {
        var any: T
        synchronized(tags) {
            any = (tags[tag] as? T) ?: block.invoke()
            tags.put(tag, any)
        }
        return any
    }

    inline fun <reified T : Any> putAny(tag: String, t: T) {
        synchronized(tags) {
            tags.put(tag, t)
        }
    }

    inline fun <reified T> getAnyIf(block: (String, Any) -> Boolean): List<T> {
        val list: MutableList<T> = ArrayList()
        synchronized(tags) {
            tags.forEach {
                if (block.invoke(it.key, it.value)) list.add(it.value as T)
            }
        }
        return list
    }

    inline fun <reified T : Any> getAny(tag: String): T? {
        var any: T?
        synchronized(tags) {
            any = tags[tag] as? T
        }
        return any
    }

    protected fun removeAny(tag: String) {
        synchronized(tags) {
            tags.remove(tag)
        }
    }

    open fun getCoroutineScope(key: String, context: CoroutineContext = Dispatchers.IO): CoroutineScope {
        var scope: CoroutineScope? = this.getTag(key)
        if (scope == null) {
            scope = setTagIfAbsent(key, CloseableCoroutineScope(SupervisorJob() + context))
        }
        return scope
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressWarnings("unchecked")
    open fun <T : Any> setTagIfAbsent(key: String, newValue: T): T {
        var previous: T?
        synchronized(mBagOfTags) {
            previous = mBagOfTags[key] as? T
            if (previous == null) {
                mBagOfTags[key] = newValue
            }
        }
        val result: T = if (previous == null) newValue else previous!!
        if (mCleared) {
            closeWithRuntimeException(result)
        }
        return result
    }

    @SuppressWarnings("TypeParameterUnusedInFormals", "unchecked")
    open fun <T : Any> getTag(key: String?): T? {
        synchronized(mBagOfTags) {
            return mBagOfTags[key] as? T
        }
    }

    private fun closeWithRuntimeException(obj: Any?) {
        if (obj is Closeable) {
            try {
                obj.close()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    fun <X, Y> LiveData<X>.switchMapLiveDataEmit(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend (X) -> Y
    ): LiveData<Y> = switchMap {
        androidx.lifecycle.liveData(handler + dispatcher) {
            emit(block(it))
        }
    }

    fun <X, Y> LiveData<X>.switchMapLiveData(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend LiveDataScope<Y>.(X) -> Unit
    ): LiveData<Y> = switchMap {
        androidx.lifecycle.liveData<Y>(handler + dispatcher) {
            block(this@liveData, it)
        }
    }
}