package com.one.coreapp.data.cache.sharedpreference

import com.one.coreapp.data.cache.NotificationCache

class NotificationCacheImpl : BaseCacheImpl(), NotificationCache {

    override fun getTimeShow(id: String): String {

        return getString("Notification-$id") ?: ""
    }

    override fun saveTimeShow(id: String, data: String) {

        putString("Notification-$id", data)
    }

}