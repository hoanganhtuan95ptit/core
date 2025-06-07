@file:Suppress("UNCHECKED_CAST")

package com.simple.core.utils.extentions

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper


val objectMapper: ObjectMapper by lazy {

    ObjectMapper()
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.DEFAULT)
        .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

inline fun <reified T> String.toObject(): T {

    return objectMapper.readValue(this, object : TypeReference<T>() {})
}

inline fun <reified T> String?.toListOrEmpty(): List<T> {

    return toObjectOrNull<List<T>>() ?: emptyList()
}

inline fun <reified T> String?.toObjectOrNull(): T? {

    if (isNullOrBlank()) return null

    return toObject()
}


fun String.toTree(): JsonNode {

    return if (this.contains("{")) {

        objectMapper.readTree(this)
    } else {

        objectMapper.readTree("{}")
    }
}


fun Any?.toJson(): String {

    if (this == null) return ""

    return objectMapper.writeValueAsString(this)
}

inline fun <reified T> Any?.asObject(): T {

    return this as T
}

inline fun <reified T> Any?.asObjectOrNull(): T? {

    return this as? T
}

inline fun <reified T> Any?.asObjectOrDefault(t: T): T {

    return asObjectOrNull() ?: t
}


inline fun <reified T> Any?.asListOrNull(needCheck: Boolean = false): Collection<T>? {

    return if (this !is Collection<*>) null
    else asListOrNull(needCheck)
}

inline fun <reified T> Any?.asListOrDefault(needCheck: Boolean = false, list: Collection<T>): Collection<T> {

    return asListOrNull(needCheck) ?: list
}

inline fun <reified T> Any?.asListOrEmpty(needCheck: Boolean = false): Collection<T> {

    return asListOrDefault(needCheck, emptyList())
}


inline fun <reified T> Collection<*>?.asListOrNull(needCheck: Boolean = false): Collection<T>? {

    return if (this == null) null
    else if (needCheck && any { it is T }) null
    else this as? Collection<T>
}

inline fun <reified T> Collection<*>?.asListOrDefault(needCheck: Boolean = false, list: Collection<T>): Collection<T> {

    return asListOrNull(needCheck) ?: list
}

inline fun <reified T> Collection<*>?.asListOrEmpty(needCheck: Boolean = false): Collection<T> {

    return asListOrDefault(needCheck, emptyList())
}