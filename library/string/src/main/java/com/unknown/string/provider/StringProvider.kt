package com.unknown.string.provider

import androidx.fragment.app.FragmentActivity
import com.simple.autobind.AutoBind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface StringProvider {

    fun priority(): Int = 0

    suspend fun provide(activity: FragmentActivity): Flow<Map<String, String>>

    companion object{

        val instant = AutoBind.loadAsync(StringProvider::class.java, true).map { list ->

            list.sortedBy { it.priority() }
        }
    }
}