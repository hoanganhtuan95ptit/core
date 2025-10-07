package com.unknown.theme.provider

import androidx.fragment.app.FragmentActivity
import com.simple.autobind.AutoBind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ThemeProvider {

    fun priority(): Int = 0

    suspend fun provide(activity: FragmentActivity): Flow<Map<String, Any>>

    companion object{

        val instant = AutoBind.loadAsync(ThemeProvider::class.java, true).map { list ->

            list.sortedBy { it.priority() }
        }
    }
}