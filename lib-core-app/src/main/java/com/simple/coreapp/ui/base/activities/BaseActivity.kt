package com.simple.coreapp.ui.base.activities

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.simple.coreapp.ui.ComponentCache
import com.simple.coreapp.ui.base.fragments.BackPressedView
import com.simple.coreapp.ui.dialogs.ConfirmDialogFragment
import com.simple.coreapp.ui.dialogs.LoadingDialog
import com.simple.coreapp.ui.worker.NotificationDateWorker
import com.simple.coreapp.utils.extentions.getAllFragment
import com.simple.crashlytics.logCrashlytics
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity(), ComponentCache {

    override val mBagOfTags: HashMap<String, Any> = hashMapOf()

    open var isEnableScheduleNotify: Boolean = false

    val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
        logCrashlytics(throwable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isEnableScheduleNotify) NotificationDateWorker.schedule()
    }

    override fun onBackPressed() {

        val list = supportFragmentManager.getAllFragment()

        if (list.reversed().any { (it as? BackPressedView)?.onBackPressed() == true }) {
            return
        }

        super.onBackPressed()
    }

    open fun showConfirm(
        title: String, message: String,
        positive: String = "", negative: String = "",
        cancelable: Boolean = true, image: Int = 0,
        listenerPositive: View.OnClickListener? = null, listenerNegative: View.OnClickListener? = null
    ): Unit = kotlin.runCatching {
        ConfirmDialogFragment.newInstance(supportFragmentManager, title, message, negative, positive, cancelable, image, listenerNegative, listenerPositive)

        Unit
    }.getOrElse {
        Unit
    }

    open fun showLoading(@StringRes message: Int = -1) {
        if (message == -1) {
            LoadingDialog.getDialog(this).show()
        } else {
            LoadingDialog.getDialog(this, message).show()
        }
    }

    open fun hideLoading() {
        LoadingDialog.dismiss(this)
    }
}