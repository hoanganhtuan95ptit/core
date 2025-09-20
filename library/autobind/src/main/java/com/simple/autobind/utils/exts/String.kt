package com.simple.autobind.utils.exts

fun <T> String.createObject(clazz: Class<T>) = runCatching {

    val instance = Class.forName(this).getDeclaredConstructor().newInstance()
    if (clazz.isInstance(instance)) clazz.cast(instance) else null
}.getOrElse {

    null
}