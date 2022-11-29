@file:Suppress("UNCHECKED_CAST")

package com.one.coreapp.utils.extentions

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

fun <T : E, E : Any> Any.findGenericClassBySuperClass(eClazz: Class<E>): KClass<T>? = findGenericClassByCause(this::class.java) {

    var type: Type? = it

    if (type is ParameterizedType) {

        type = type.rawType
    }

    if (type is Class<*> && type != Object::class.java && eClazz.isAssignableFrom(type)) {

        return@findGenericClassByCause type as Class<T>
    } else {

        return@findGenericClassByCause null
    }
}?.kotlin

fun <T : E, E : Any> findGenericClassByCause(currentClazz: Class<*>, cause: (Type) -> Class<T>?): Class<T>? {

    val superClass = currentClazz.genericSuperclass

    if (superClass !is ParameterizedType) {

        return null
    }

    superClass.actualTypeArguments.forEach { type ->

        cause.invoke(type)?.let {
            return it
        }
    }

    if (superClass.rawType is Class<*> && superClass.rawType != Object::class.java) {

        return findGenericClassByCause(superClass.rawType as Class<*>, cause)
    }

    return null
}