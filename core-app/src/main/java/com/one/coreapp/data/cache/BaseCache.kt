package com.one.coreapp.data.cache

import kotlinx.coroutines.flow.Flow

interface BaseCache {

    fun getInt(key: String, defaultValue: Int? = null): Int?

    fun getLong(key: String, defaultValue: Long? = null): Long?

    fun getString(key: String, defaultValue: String? = null): String?

    fun getFloat(key: String, defaultValue: Float? = null): Float?

    fun getBoolean(key: String, defaultValue: Boolean? = null): Boolean?

    fun getIntFlow(key: String, defaultValue: Int? = null): Flow<Int?>

    fun getLongFlow(key: String, defaultValue: Long? = null): Flow<Long?>

    fun getStringFlow(key: String, defaultValue: String? = null): Flow<String?>

    fun getFloatFlow(key: String, defaultValue: Float? = null): Flow<Float?>

    fun getBooleanFlow(key: String, defaultValue: Boolean? = null): Flow<Boolean?>

    operator fun contains(key: String): Boolean

    fun putInt(key: String, value: Int)

    fun putLong(key: String, value: Long)

    fun putFloat(key: String, value: Float)

    fun putString(key: String, value: String?)

    fun putBoolean(key: String, value: Boolean)

    fun remove(key: String)

    fun clear()

}