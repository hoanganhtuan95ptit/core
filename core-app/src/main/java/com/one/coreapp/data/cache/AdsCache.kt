package com.one.coreapp.data.cache

interface AdsCache : BaseCache {
    fun getTimeClick(): Long

    fun saveTimeClick(time: Long)

    fun getTimeShow(): Long

    fun saveTimeShow(time: Long)

    fun getCount(): Long

    fun saveCount(count: Long)
}