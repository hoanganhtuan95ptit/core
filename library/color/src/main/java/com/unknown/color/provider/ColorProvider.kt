package com.unknown.color.provider

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.Flow

interface ColorProvider {

    fun priority(): Int = 0

    suspend fun provide(activity: FragmentActivity): Flow<Map<String, Int>>
}