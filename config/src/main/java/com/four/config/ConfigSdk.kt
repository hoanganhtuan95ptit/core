package com.four.config

import com.one.state.ResultState
import com.one.task.executeAsyncByFast
import org.koin.java.KoinJavaComponent

suspend fun getConfig(param: Config.Param): String {

    val listConfig = KoinJavaComponent.getKoin().getAll<Config>()

    return (listConfig.executeAsyncByFast(param) as? ResultState.Success)?.data ?: param.default
}

