package com.one.coreapp.data.cache

interface ReviewCache : BaseCache {

    fun getCount(): Long

    fun saveCount(count: Long)
}