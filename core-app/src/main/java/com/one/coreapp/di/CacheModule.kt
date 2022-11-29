@file:Suppress("USELESS_CAST")

package com.one.coreapp.di

import com.one.coreapp.data.api.retrofit.ConfigApi
import com.one.coreapp.data.cache.NotificationCache
import com.one.coreapp.data.cache.sharedpreference.NotificationCacheImpl
import org.koin.dsl.module
import retrofit2.Retrofit


/**
 * cache
 */
@JvmField
val coreCacheModule = module {

    single { NotificationCacheImpl() as NotificationCache }

    single { (get() as Retrofit).create(ConfigApi::class.java) }
}
