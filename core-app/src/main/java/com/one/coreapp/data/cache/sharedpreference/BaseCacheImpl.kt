package com.one.coreapp.data.cache.sharedpreference

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.one.core.utils.extentions.toJson
import com.one.core.utils.extentions.toObject
import com.one.coreapp.App
import com.one.coreapp.data.cache.BaseCache
import com.one.coreapp.utils.extentions.decodeBase64
import com.one.coreapp.utils.extentions.encodeBase64
import com.one.coreapp.utils.extentions.encodeMd5
import com.one.coreapp.utils.extentions.offerActive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

private val keyCache by lazy {
    encodeMd5(App.shared.packageName)
}

private val sharedPreferences by lazy {
    PreferenceManager.getDefaultSharedPreferences(App.shared)
}

abstract class BaseCacheImpl : BaseCache {

    private inline fun <reified T : Any> saveOrUpdate(key: String, data: T?) {
        val dataStr = when (T::class) {
            Double::class -> data?.toString()
            String::class -> data?.toString()
            Float::class -> data?.toString()
            Long::class -> data?.toString()
            Int::class -> data?.toString()
            else -> data?.toJson()
        }
        sharedPreferences.edit().putString(key, encode(dataStr)).apply()
    }

    @Suppress("NAME_SHADOWING")
    private inline fun <reified T> find(key: String): T? {
        val data = decode(sharedPreferences.getString(key, null))
        return when (T::class) {
            Double::class -> data?.toDouble() as? T?
            String::class -> data?.toString() as? T?
            Float::class -> data?.toFloat() as? T?
            Long::class -> data?.toLong() as? T?
            Int::class -> data?.toInt() as? T?
            else -> data?.toObject(T::class.java)
        }
    }

    private inline fun <reified T> findFlow(key: String): Flow<T?> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
            if (k == key) {
                offerActive("")
            }
        }

        offerActive("")

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.map {
        find<T>(key)
    }

    private fun encode(data: String?): String? {
        if (data.isNullOrEmpty()) return null
        return encodeBase64(keyCache + encodeBase64(data))
    }

    private fun decode(data: String?): String? {
        if (data.isNullOrEmpty()) return null
        return decodeBase64(decodeBase64(data).replace(keyCache, ""))
    }

    override fun getInt(key: String, defaultValue: Int?): Int? = find(key) ?: defaultValue

    override fun getLong(key: String, defaultValue: Long?): Long? = find(key) ?: defaultValue

    override fun getString(key: String, defaultValue: String?): String? = find(key) ?: defaultValue

    override fun getFloat(key: String, defaultValue: Float?): Float? = find(key) ?: defaultValue

    override fun getBoolean(key: String, defaultValue: Boolean?): Boolean? = find(key) ?: defaultValue

    override fun getIntFlow(key: String, defaultValue: Int?): Flow<Int?> = findFlow<Int>(key).map { it ?: defaultValue }

    override fun getLongFlow(key: String, defaultValue: Long?): Flow<Long?> = findFlow<Long>(key).map { it ?: defaultValue }

    override fun getStringFlow(key: String, defaultValue: String?): Flow<String?> = findFlow<String>(key).map { it ?: defaultValue }

    override fun getFloatFlow(key: String, defaultValue: Float?): Flow<Float?> = findFlow<Float>(key).map { it ?: defaultValue }

    override fun getBooleanFlow(key: String, defaultValue: Boolean?): Flow<Boolean?> = findFlow<Boolean>(key).map { it ?: defaultValue }

    override fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun putInt(key: String, value: Int) = saveOrUpdate(key, value)

    override fun putLong(key: String, value: Long) = saveOrUpdate(key, value)

    override fun putFloat(key: String, value: Float) = saveOrUpdate(key, value)

    override fun putString(key: String, value: String?) = saveOrUpdate(key, value)

    override fun putBoolean(key: String, value: Boolean) = saveOrUpdate(key, value)

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}