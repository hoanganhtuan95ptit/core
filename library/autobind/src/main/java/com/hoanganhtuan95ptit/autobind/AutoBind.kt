@file:Suppress("MemberVisibilityCanBePrivate")

package com.hoanganhtuan95ptit.autobind

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hoanganhtuan95ptit.autobind.utils.exts.createObject
import com.hoanganhtuan95ptit.autobind.utils.exts.distinctPattern
import com.hoanganhtuan95ptit.autobind.utils.exts.reloadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object AutoBind {

    lateinit var application: Application


    private val map = ConcurrentHashMap<String, List<String>>()

    private val loadState = MutableStateFlow(false)


    fun init(application: Application) {

        if (::application.isInitialized) {
            return
        }

        this.application = application

        reload()
    }

    fun reload() = ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {

        loadState.tryEmit(false)

        val allBindings = this@AutoBind.application.reloadBinding()

        allBindings.groupBy({

            it.type
        }, {

            it.impl
        }).mapValues {

            it.value.toSet().toList()
        }.let {

            map.putAll(it)
        }

        loadState.tryEmit(true)
    }

    fun forceLoad() {

        reload()
    }


    fun <T> load(clazz: Class<T>): List<T> =
        map[clazz.name].createObject(clazz)

    fun <T> loadAsync(clazz: Class<T>, distinctPattern: Boolean = true): Flow<List<T>> =
        loadNamesFlow(clazz, distinctPattern).map { it.createObject(clazz) }


    fun <T> loadName(clazz: Class<T>): List<String> =
        map[clazz.name].orEmpty()

    fun <T> loadNameAsync(clazz: Class<T>, distinctPattern: Boolean = true): Flow<List<String>> =
        loadNamesFlow(clazz, distinctPattern)


    suspend fun awaitLoaded() = loadState.filter { it }.first()


    private fun <T> List<String>?.createObject(clazz: Class<T>) = this?.mapNotNull { it.createObject(clazz) }.orEmpty()

    private fun <T> loadNamesFlow(clazz: Class<T>, distinctPattern: Boolean = false): Flow<List<String>> {

        var flow: Flow<List<String>> = loadState
            .filter { it }                 // chỉ lấy khi true
            .map { loadName(clazz) }       // map sang list

        if (distinctPattern) {
            flow = flow.distinctPattern()
        }

        return flow
    }
}