package com.hoanganhtuan95ptit.autobind.utils.exts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun Flow<List<String>>.distinctPattern() = flow {

    val handled = mutableSetOf<String>()

    collect { list ->

        val listFilter = list.filter {
            handled.add(it)
        }

        if (listFilter.isNotEmpty()) {

            emit(listFilter)
        }
    }
}
