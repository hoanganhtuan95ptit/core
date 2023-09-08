package com.one.ads

import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.one.coreapp.BaseApp
import com.one.coreapp.data.cache.AdsCache
import com.one.coreapp.data.cache.sharedpreference.AdsCacheImpl
import com.one.coreapp.ui.base.activities.BaseActivity
import com.one.analytics.logAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import kotlin.coroutines.resume

private val module = module {

    single<AdsCache> { AdsCacheImpl() }
}

private val loadKoinModules by lazy {

    loadKoinModules(module)
}

private fun injectAdsModule() = loadKoinModules


interface AdsView {

    val adsCache: AdsCache

    var adsEnable: Boolean

    fun initAds() {

        injectAdsModule()

        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR, "374F5B690FEEBDC16F6554123B66103A", "726A603E157364E72B59FF3D2E9CB11B"))
            .build()

        MobileAds.setRequestConfiguration(requestConfiguration)

        MobileAds.initialize(BaseApp.shared) {}
    }

    fun checkAndShowAds(show: Boolean) = self().lifecycleScope.launch(self().handler + Dispatchers.IO) {

        if (!adsEnable) {
            return@launch
        }

        val count = adsCache.getCount() + 1
        adsCache.saveCount(count)

        val isShow = show || canShowAds(count)


        if (!isShow) {
            return@launch
        }


        val interstitialAd = suspendCancellableCoroutine<InterstitialAd?> { cancellableContinuation ->

            launch(coroutineContext + Dispatchers.Main) {

                InterstitialAd.load(BaseApp.shared, self().getString(R.string.key_interstitial), AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {

                        if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(interstitialAd)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {

                        if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(null)
                    }
                })
            }
        } ?: return@launch


        suspendCancellableCoroutine<String?> { cancellableContinuation ->

            launch(coroutineContext + Dispatchers.Main) {

                interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {

                    override fun onAdDismissedFullScreenContent() {

                        if (!cancellableContinuation.isCompleted) cancellableContinuation.resume("")
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {

                        if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(null)
                    }
                }

                interstitialAd.show(self())
            }
        } ?: return@launch

        logAnalytics("ads")
    }

    open fun canShowAds(count: Long): Boolean {
        return count.toString() != "0" && count.rem(3).toString() == "0"
    }

    private fun self() = this as BaseActivity
}