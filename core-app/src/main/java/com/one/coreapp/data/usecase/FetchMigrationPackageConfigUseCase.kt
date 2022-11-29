package com.one.coreapp.data.usecase

import com.one.coreapp.App
import com.one.coreapp.utils.ConfigUtils

class FetchMigrationPackageConfigUseCase : BaseUseCase<FetchMigrationPackageConfigUseCase.Param, String> {

    override suspend fun execute(param: Param?): String {

        val keyName = App.shared.packageName.replace(".", "")

        return ConfigUtils.configAsync(keyName)
    }

    class Param : BaseUseCase.Param()
}