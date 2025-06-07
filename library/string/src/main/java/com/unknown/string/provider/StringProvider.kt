package com.unknown.string.provider

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.Flow

interface StringProvider {

    fun priority(): Int = 0

    suspend fun provide(activity: FragmentActivity): Flow<Map<String, String>>
}