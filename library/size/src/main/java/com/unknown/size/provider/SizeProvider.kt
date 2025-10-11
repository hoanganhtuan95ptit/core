package com.unknown.size.provider

import androidx.fragment.app.FragmentActivity
import com.simple.autobind.AutoBind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SizeProvider {

    fun priority(): Int = 0

    suspend fun provide(activity: FragmentActivity): Flow<Map<String, Int>>

    companion object{

        val instant = AutoBind.loadAsync(SizeProvider::class.java, true).map { list ->

            list.sortedBy { it.priority() }
        }
    }
}