package com.simple.coreapp.data.cache.sharedpreference

import com.simple.coreapp.data.cache.ReviewCache

class ReviewCacheImpl : BaseCacheImpl(), ReviewCache {

    override fun getCount(): Long {
        return getLong("Review-Count") ?: 0
    }

    override fun saveCount(count: Long) {
        putLong("Review-Count", count)
    }
}