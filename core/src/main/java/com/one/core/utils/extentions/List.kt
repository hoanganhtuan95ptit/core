package com.one.core.utils.extentions


public fun <T> List<T>.getLastOrDefault(t: T): T = lastOrDefault(t)

public fun <T> List<T>.getLastOrNull(): T? = lastOrNull()


public fun <T> List<T>.lastOrDefault(t: T): T = getLastOrNull() ?: t

public fun <T> List<T>.lastOrNull(): T? = getOrNull(size - 1)

public fun <T> List<T>.index(cause:(T)->Boolean): Int {
    var index = -1;

    forEachIndexed { i, it ->
        if(cause.invoke(it)){
            index = i
            return@forEachIndexed
        }
    }

    return index
}