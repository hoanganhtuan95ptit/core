package com.one.coreapp.utils.extentions

import com.one.coreapp.App
import com.one.coreapp.data.task.config.Config
import com.one.coreapp.data.usecase.ResultState
import com.one.task.executeAsyncByFast

suspend fun getConfig(param: Config.Param): String {

    return (App.shared.configs.executeAsyncByFast(param) as? ResultState.Success)?.data ?: param.default
}

