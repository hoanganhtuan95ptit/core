package com.one.coreapp.ui.base.activities

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.one.coreapp.ui.base.fragments.BackPressedView
import com.one.coreapp.ui.dialogs.ConfirmDialogFragment
import com.one.coreapp.ui.dialogs.LoadingDialog
import com.one.coreapp.ui.worker.NotificationDateWorker
import com.one.coreapp.utils.extentions.getAllFragment
import com.one.coreapp.utils.extentions.logException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity() {

    open var isEnableScheduleNotify: Boolean = false

    val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
        logException(throwable)
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