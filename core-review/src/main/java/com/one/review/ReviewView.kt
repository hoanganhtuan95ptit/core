package com.one.review

import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.one.coreapp.BuildConfig
import com.one.coreapp.data.cache.ReviewCache
import com.one.coreapp.data.cache.sharedpreference.ReviewCacheImpl
import com.one.coreapp.ui.base.activities.BaseActivity
import com.one.coreapp.utils.extentions.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import kotlin.coroutines.resume

private val module = module {

    single<ReviewCache> { ReviewCacheImpl() }
}

private val loadKoinModules by lazy {

    loadKoinModules(module)
}

private fun injectReviewModule() = loadKoinModules


interface ReviewView {

    val reviewCache: ReviewCache

    var reviewEnable: Boolean

    fun initReview() {

        injectReviewModule()
    }

    fun checkAndShowReview(show: Boolean) = self().lifecycleScope.launch(self().handler + Dispatchers.IO) {

        if (!reviewEnable) {
            return@launch
        }

        val count = reviewCache.getCount() + 1
        reviewCache.saveCount(count)

        val isShow = show || canShowReview(count)


        if (!isShow) {
            return@launch
        }


        val manager: ReviewManager = suspendCancellableCoroutine { cancellableContinuation ->

            launch(coroutineContext + Dispatchers.Main) {

                if (BuildConfig.DEBUG) {

                    FakeReviewManager(self())
                } else {

                    ReviewManagerFactory.create(self())
                }.let {

                    if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(it)
                }
            }
        } ?: return@launch


        val request: ReviewInfo = suspendCancellableCoroutine { cancellableContinuation ->

            launch(coroutineContext + Dispatchers.Main) {

                manager.requestReviewFlow().addOnSuccessListener { info ->

                    if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(info)
                }.addOnFailureListener {

                    if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(null)
                }
            }
        } ?: return@launch


        suspendCancellableCoroutine<String?> { cancellableContinuation ->

            launch(coroutineContext + Dispatchers.Main) {

                manager.launchReviewFlow(self(), request).addOnSuccessListener {

                    if (!cancellableContinuation.isCompleted) cancellableContinuation.resume("")
                }.addOnFailureListener {

                    if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(null)
                }
            }
        } ?: return@launch

        log("review")
    }

    open fun canShowReview(count: Long): Boolean {
        return true
    }

    private fun self() = this as BaseActivity
}