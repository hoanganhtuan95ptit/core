package com.one.coreapp.data.dao

import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

interface BaseDao<T> : KoinComponent {

    fun saveOrUpdate(vararg ts: T)

    fun findAll(): List<T>

    fun findAllSync(): Flow<List<T>>

}