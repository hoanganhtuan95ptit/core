package com.simple.coreapp.utils.ext

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable


inline fun <reified T> Bundle.getSerializableListOrNull(key: String): List<T>? {

    return getSerializableOrNull<ArrayList<T>>(key)
}

inline fun <reified T : Serializable> Bundle.getSerializableOrNull(key: String): T? = kotlin.runCatching {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}.getOrNull()

inline fun <reified T : Parcelable> Bundle.getParcelableListOrNull(key: String): List<T>? = kotlin.runCatching {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, T::class.java)
    } else {
        getParcelableArrayList<T>(key)
    }
}.getOrNull()

inline fun <reified T : Parcelable> Bundle.getParcelableOrNull(key: String): T? = kotlin.runCatching {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key) as? T
    }
}.getOrNull()


fun Bundle?.getStringOrEmpty(key: String): String {

    return this?.getString(key, "") ?: ""
}


inline fun <reified T> Intent.getSerializableListOrNull(key: String): List<T>? {

    return getSerializableOrNull<ArrayList<T>>(key)
}

inline fun <reified T : Serializable> Intent.getSerializableOrNull(key: String): T? = kotlin.runCatching {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        getSerializableExtra(key) as? T
    }
}.getOrNull()

inline fun <reified T : Parcelable> Intent.getParcelableListOrNull(key: String): List<T>? = kotlin.runCatching {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayListExtra(key, T::class.java)
    } else {
        getParcelableArrayListExtra<T>(key)
    }
}.getOrNull()

inline fun <reified T : Parcelable> Intent.getParcelableOrNull(key: String): T? = kotlin.runCatching {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else {
        getParcelableExtra(key) as? T
    }
}.getOrNull()
