@file:Suppress("DEPRECATION")

package com.one.coreapp.data.usecase

import android.content.pm.PackageInfo
import android.os.Build
import com.one.coreapp.App
import com.one.coreapp.utils.ConfigUtils
import com.one.coreapp.utils.extentions.offerActiveAwait
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

val versionCode: Long by lazy {

    val packageInfo: PackageInfo = App.shared.packageManager.getPackageInfo(App.shared.packageName, 0)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }
}

class FetchFeatureFlagUseCase : BaseUseCase<FetchFeatureFlagUseCase.Param, Flow<Boolean>> {

    override suspend fun execute(param: Param?) = channelFlow {
        checkNotNull(param)

        val versionCodeActive = ConfigUtils.configAsync(fieldName = param.featureFlagKey, time = 10 * 60L).toLongOrNull()

        val enable = if (versionCodeActive == null) {
            param.defaultValue
        } else {
            versionCodeActive < versionCode
        }

        offerActiveAwait(enable)
    }

    class Param(val featureFlagKey: String, val defaultValue: Boolean) : BaseUseCase.Param()
}