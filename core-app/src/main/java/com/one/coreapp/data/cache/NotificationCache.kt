package com.one.coreapp.data.cache

interface NotificationCache : BaseCache {

    fun getTimeShow(id: String): String

    fun saveTimeShow(id: String, data: String)
}