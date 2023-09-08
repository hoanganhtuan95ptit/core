package com.one.update

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.one.coreapp.BaseApp
import com.one.coreapp.BuildConfig
import com.one.coreapp.Constants
import com.one.coreapp.R
import com.one.coreapp.data.usecase.FetchMigrationPackageConfigUseCase
import com.one.state.ResultState
import com.one.coreapp.ui.base.activities.BaseActivity
import com.one.coreapp.utils.Utils
import com.one.analytics.logAnalytics
import com.one.coreapp.utils.extentions.offerActive
import com.one.coreapp.utils.extentions.offerActiveAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val module = module {

    single { FetchMigrationPackageConfigUseCase() }
}

private val loadKoinModules by lazy {

    loadKoinModules(module)
}

fun injectUpdateModule() = loadKoinModules

interface UpdateView {

    val fetchMigrationPackageConfigUseCase: FetchMigrationPackageConfigUseCase

    var updateEnable: Boolean

    fun initUpdate() {

        injectUpdateModule()

        if (updateEnable) self().lifecycleScope.launch(self().handler + Dispatchers.Main) {

            update().distinctUntilChanged().flowOn(Dispatchers.Main).collect {
                if (it is ResultState.Success && it.data == Constants.ACTION_UPDATE)
                    openUpdate()
                else if (it is ResultState.Success && it.data.isNotBlank())
                    openMigration(it.data)
            }
        }
    }

    private fun update(): Flow<ResultState<String>> = channelFlow {

        if (BuildConfig.DEBUG) Log.d(TAG, "update: ")

        /**
         * kiểm tra app có cần migration không
         */
        val packageName = fetchMigrationPackageConfigUseCase.execute()
        if (packageName.isNotBlank()) {

            if (BuildConfig.DEBUG) Log.d(TAG, "update: $packageName")
            offerActiveAwait(ResultState.Success(packageName))
            return@channelFlow
        }

        /**
         * kiểm tra update app
         */
        val appUpdateManager = if (BuildConfig.DEBUG) {
            FakeAppUpdateManager(BaseApp.shared).apply { setUpdateAvailable(0) }
        } else {
            AppUpdateManagerFactory.create(BaseApp.shared)
        }

        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE || it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                offerActive(ResultState.Success(Constants.ACTION_UPDATE))
            }
        }

        val installStateUpdatedListener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate()
            }
        }

        appUpdateManager.registerListener(installStateUpdatedListener)

        awaitClose {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    private fun openUpdate() = self().showConfirm(self().getString(R.string.title_core_update_app), self().getString(R.string.message_core_update_app), image = R.raw.img_core_update, positive = self().getString(R.string.action_core_update), listenerPositive = {

        logAnalytics("app-update", "")

        Utils.updateApp(self())
    })

    private fun openMigration(packageName: String) = self().showConfirm(self().getString(R.string.title_core_migration_app), self().getString(R.string.message_core_migration_app), image = R.raw.img_core_update, positive = self().getString(R.string.action_core_migration), listenerPositive = {

        logAnalytics("app-migration", "")

        Utils.openApp(self(), packageName)
    })

    private fun self() = this as BaseActivity

    companion object {
        private const val TAG = "BaseUpdateActivity"
    }
}