package com.one.coreapp.data.cache.sharedpreference

import com.one.coreapp.data.cache.AdsCache

class AdsCacheImpl : BaseCacheImpl(), AdsCache {

    override fun getTimeClick(): Long {
        return getLong("TimeClick") ?: 0
    }

    override fun saveTimeClick(time: Long) {
        putLong("TimeClick", time)
    }

    override fun getTimeShow(): Long {
        return getLong("TimeShow") ?: 0
    }

    override fun saveTimeShow(time: Long) {
        putLong("TimeShow", time)
    }

    override fun getCount(): Long {
        return getLong("Ads-Count") ?: 0
    }

    override fun saveCount(count: Long) {
        putLong("Ads-Count", count)
    }
}