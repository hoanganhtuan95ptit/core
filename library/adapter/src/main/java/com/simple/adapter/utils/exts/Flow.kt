package com.simple.adapter.utils.exts

import com.hoanganhtuan95ptit.autobind.AutoBind
import com.simple.adapter.ViewItemAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.attachToAdapter() = flow {

    combine(
        this@attachToAdapter,
        AutoBind.loadNameAsync(ViewItemAdapter::class.java, true)
    ) { data, _ ->

        data
    }.collect {

        emit(it)
    }
}