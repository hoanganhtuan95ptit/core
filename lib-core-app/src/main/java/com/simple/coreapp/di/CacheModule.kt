@file:Suppress("USELESS_CAST")

package com.simple.coreapp.di

import com.simple.coreapp.data.api.retrofit.ConfigApi
import com.simple.coreapp.data.cache.NotificationCache
import com.simple.coreapp.data.cache.sharedpreference.NotificationCacheImpl
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
