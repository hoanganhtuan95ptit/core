package com.one.coreapp.data.repository

import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent

interface BaseRepository<T> : KoinComponent {

    fun saveOrUpdate(vararg ts: T)

    fun findAll(): List<T>

    fun findAllSync(): Flow<List<T>>
}