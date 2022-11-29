package com.one.coreapp.ui.base.activities

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.one.coreapp.ui.base.dialogs.BaseSheetFragment
import com.one.coreapp.ui.base.fragments.BaseFragment
import com.one.coreapp.ui.dialogs.ConfirmDialogFragment
import com.one.coreapp.ui.dialogs.LoadingDialog
import com.one.coreapp.ui.worker.NotificationDateWorker
import com.one.coreapp.utils.Analytics
import com.one.coreapp.utils.extentions.findThisNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity() {

    open var isEnableScheduleNotify: Boolean = false

    val handler = CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
        Analytics.logException(throwable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isEnableScheduleNotify) NotificationDateWorker.schedule()
    }

    override fun onBackPressed() {

        val list = arrayListOf<Fragment>()

        getFragments(supportFragmentManager, list)

        var back = list.isEmpty() || list.all {

            when (it) {
                is BaseFragment -> !it.onBackPressed()
                is BaseSheetFragment -> !it.onBackPressed()
                else -> true
            }
        }

        if (back) {
            back = !findThisNavController().navigateUp()
        }

        if (back) {
            super.onBackPressed()
        }
    }

    private fun getFragments(fragmentManager: FragmentManager, list: ArrayList<Fragment>) {

        fragmentManager.fragments.forEach {

            getFragments(it.childFragmentManager, list)

            if (it is BaseFragment || it is BaseSheetFragment) {
                list.add(it)
            }
        }
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