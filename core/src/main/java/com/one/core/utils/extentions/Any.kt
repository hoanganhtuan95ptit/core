package com.one.core.utils.extentions

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

fun <T> String.toObject(clazz: Class<T>): T {
    return ObjectMapperProvider.instance.objectMapper.readValue(this, clazz)
}

fun <T> String.toObjectOrNull(clazz: Class<T>): T? {
    if (isNullOrBlank()) return null

    return ObjectMapperProvider.instance.objectMapper.readValue(this, clazz)
}

fun <T> String.toListObject(clazz: Class<T>): List<T> {
    if (isNullOrBlank()) return emptyList()

    val listType = ObjectMapperProvider.instance.objectMapper.typeFactory.constructCollectionType(List::class.java, clazz)
    return ObjectMapperProvider.instance.objectMapper.readValue(this, listType)
}

fun <T> String.toListObjectOrEmpty(clazz: Class<T>): List<T> {
    if (isNullOrBlank()) return emptyList()

    val listType = ObjectMapperProvider.instance.objectMapper.typeFactory.constructCollectionType(List::class.java, clazz)
    return ObjectMapperProvider.instance.objectMapper.readValue(this, listType)
}


fun Any?.toJson(): String {
    if (this == null) return ""

    return ObjectMapperProvider.instance.objectMapper.writeValueAsString(this)
}
