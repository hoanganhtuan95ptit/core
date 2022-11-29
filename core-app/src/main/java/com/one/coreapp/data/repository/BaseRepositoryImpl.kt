package com.one.coreapp.data.repository

import com.one.coreapp.data.dao.BaseDao
import kotlinx.coroutines.flow.Flow

abstract class BaseRepositoryImpl<T, D : BaseDao<T>> : BaseRepository<T> {

    protected abstract val database: D

    override fun saveOrUpdate(vararg ts: T) {
        database.saveOrUpdate(*ts)
    }

    override fun findAll(): List<T> {
        return database.findAll()
    }

    override fun findAllSync(): Flow<List<T>> {
        return database.findAllSync()
    }
}