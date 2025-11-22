package com.unknown.color.provider

import androidx.fragment.app.FragmentActivity
import com.simple.autobind.AutoBind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ColorProvider {

    fun priority(): Int = 0

    suspend fun provide(activity: FragmentActivity): Flow<Map<String, Int>>

    companion object {

        val instant = AutoBind.loadAsync(ColorProvider::class.java, true).map { list ->

            list.sortedBy { it.priority() }
        }
    }
}