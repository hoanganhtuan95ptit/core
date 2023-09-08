@file:Suppress("NO_REFLECTION_IN_CLASS_PATH")

package com.simple.core.utils.extentions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions

class ObjectMapperProvider {

    private object HOLDER {
        val INSTANCE = ObjectMapperProvider()
    }

    companion object {
        val instance: ObjectMapperProvider by lazy { HOLDER.INSTANCE }
    }

    val objectMapper: ObjectMapper = ObjectMapper()

}

inline fun <reified T> String.toObject(): T {

    return ObjectMapperProvider.instance.objectMapper.readValue(this, object : TypeReference<T>() {})
}

inline fun <reified T> String?.toListOrEmpty(): List<T> {

    return toObjectOrNull<List<T>>() ?: emptyList()
}

inline fun <reified T> String?.toObjectOrNull(): T? {

    if (isNullOrBlank()) return null

    return toObject()
}


fun Any?.toJson(): String {

    if (this == null) return ""

    return ObjectMapperProvider.instance.objectMapper.writeValueAsString(this)
}

fun <T : Any> T.cloneOrNull(): T {

    if (!this::class.isData) {

        return this
    }

    val copy = this::class.memberFunctions.first { it.name == "copy" }

    val instanceParam = copy.instanceParameter!!

    return copy.callBy(mapOf(instanceParam to this)) as T
}