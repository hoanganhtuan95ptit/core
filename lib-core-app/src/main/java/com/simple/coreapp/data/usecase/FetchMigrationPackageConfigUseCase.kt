package com.simple.coreapp.data.usecase

import com.simple.config.Config
import com.simple.config.getConfig
import com.simple.coreapp.BaseApp

class FetchMigrationPackageConfigUseCase : BaseUseCase<FetchMigrationPackageConfigUseCase.Param, String> {

    override suspend fun execute(param: Param?): String {

        val keyName = BaseApp.shared.packageName.replace(".", "")

        return getConfig(Config.Param(keyName, "", 60 * 60L))
    }

    class Param : BaseUseCase.Param()
}