package com.one.coreapp.data.usecase

import com.four.config.Config
import com.four.config.getConfig
import com.one.coreapp.BaseApp

class FetchMigrationPackageConfigUseCase : BaseUseCase<FetchMigrationPackageConfigUseCase.Param, String> {

    override suspend fun execute(param: Param?): String {

        val keyName = BaseApp.shared.packageName.replace(".", "")

        return getConfig(Config.Param(keyName, "", 60 * 60L))
    }

    class Param : BaseUseCase.Param()
}