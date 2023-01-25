package com.one.coreapp.data.usecase

import com.one.coreapp.App
import com.one.coreapp.data.task.config.Config
import com.one.coreapp.utils.extentions.getConfig

class FetchMigrationPackageConfigUseCase : BaseUseCase<FetchMigrationPackageConfigUseCase.Param, String> {

    override suspend fun execute(param: Param?): String {

        val keyName = App.shared.packageName.replace(".", "")

        return getConfig(Config.Param(keyName, "", 60 * 60L))
    }

    class Param : BaseUseCase.Param()
}