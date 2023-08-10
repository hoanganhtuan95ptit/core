package com.one.core.utils.extentions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

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
