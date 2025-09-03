package com.unknown.theme.provider

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.Flow

interface ThemeProvider {

    fun priority(): Int = 0

    suspend fun provide(activity: FragmentActivity): Flow<Map<String, Any>>
}