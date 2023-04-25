package com.one.coreapp.ui.base.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.one.coreapp.R
import com.one.coreapp.ui.base.activities.BaseActivity
import com.one.coreapp.utils.AppException
import com.one.coreapp.utils.extentions.getColorFromAttr
import com.one.coreapp.utils.extentions.navigateUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.io.IOException
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

open class BaseFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId), BackPressedView {

    private val confirmDialogList: CopyOnWriteArrayList<AlertDialog> = CopyOnWriteArrayList<AlertDialog>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().window.navigationBarColor = view.context.getColorFromAttr(R.attr.colorNavigationBar)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        for (confirmDialog in confirmDialogList) {
            if (confirmDialog != null && confirmDialog.isShowing) confirmDialog.dismiss()
        }
    }

    @CallSuper
    open fun onRetry(action: String) {

    }

    @CallSuper
    open fun wrapError(error: Throwable): AppException = when (error) {
        is AppException -> error
        is IOException -> AppException(code = "", message = getString(R.string.message_core_error_network))
        else -> AppException(code = "", message = getString(R.string.message_core_error))
    }


    open fun showErrorPopup(error: Throwable) {
        val appException = wrapError(error)
        showErrorPopup(appException.title ?: getString(R.string.title_core_error), appException.message, appException.image)
    }

    open fun showErrorPopup(messageRes: Int) {
        showErrorPopup(getString(R.string.title_core_error), getString(messageRes))
    }

    open fun showErrorPopup(titleRes: Int, messageRes: Int) {
        showErrorPopup(getString(titleRes), getString(messageRes))
    }

    open fun showErrorPopup(titleRes: String, messageRes: String, image: Int = -1) {
        showConfirm(titleRes, messageRes, getString(R.string.action_core_close), image = image)
    }


    open fun showErrorRetry(action: String, error: Throwable) {
        val appException = wrapError(error)
        showErrorRetry(action, appException.title ?: getString(R.string.title_core_error), appException.message, appException.image)
    }

    open fun showErrorRetry(action: String, messageRes: Int) {
        showErrorRetry(action, getString(R.string.title_core_error), getString(messageRes))
    }

    open fun showErrorRetry(action: String, titleRes: Int, messageRes: Int) {
        showErrorRetry(action, getString(titleRes), getString(messageRes))
    }

    open fun showErrorRetry(action: String, titleRes: String, messageRes: String, image: Int = -1) {
        showConfirm(titleRes, messageRes, getString(R.string.action_core_retry), listenerPositive = { onRetry(action) }, image = image)
    }


    open fun showErrorNavigateUp(error: Throwable) {
        val appException = wrapError(error)
        showErrorNavigateUp(appException.title ?: getString(R.string.title_core_error), appException.message, appException.image)
    }

    open fun showErrorNavigateUp(messageRes: Int) {
        showErrorNavigateUp(getString(R.string.title_core_error), getString(messageRes))
    }

    open fun showErrorNavigateUp(titleRes: Int, messageRes: Int) {
        showErrorNavigateUp(getString(titleRes), getString(messageRes))
    }

    open fun showErrorNavigateUp(titleRes: String, messageRes: String, image: Int = -1) {
        showConfirm(titleRes, messageRes, getString(R.string.action_core_back), listenerPositive = { navigateUp() }, image = image)
    }

    protected open fun showConfirm(
        title: String, message: String,
        positive: String = "", negative: String = "",
        cancelable: Boolean = true, image: Int = 0,
        listenerPositive: View.OnClickListener? = null, listenerNegative: View.OnClickListener? = null
    ) {
        (requireActivity() as BaseActivity).showConfirm(title, message, positive, negative, cancelable, image, listenerPositive, listenerNegative)
    }

    protected open fun showConfirm(
        titleStr: String = "", messageStr: String = "",
        positiveStr: String = "", negativeStr: String = "",
        titleRes: Int = 0, messageRes: Int = 0,
        positiveRes: Int = 0, negativeRes: Int = 0,
        cancelable: Boolean = true, image: Int = 0,
        listenerPositive: View.OnClickListener? = null, listenerNegative: View.OnClickListener? = null
    ) {

        val title = getTextStrOrRes(titleStr, titleRes)

        val message = getTextStrOrRes(messageStr, messageRes)

        val positive = getTextStrOrRes(positiveStr, positiveRes)

        val negative = getTextStrOrRes(negativeStr, negativeRes)

        (requireActivity() as BaseActivity).showConfirm(title, message, positive, negative, cancelable, image, listenerPositive, listenerNegative)
    }

    private fun getTextStrOrRes(str: String = "", res: Int = 0) = if (str.isNotBlank()) {
        str
    } else if (res > 0) {
        getString(res)
    } else {
        ""
    }

    private val nameAndJob = HashMap<String, Job>()

    protected fun launch(actionName: String = UUID.randomUUID().toString(), block: suspend CoroutineScope.() -> Unit) = apply {

        nameAndJob[actionName]?.cancel()
    }.viewLifecycleOwner.lifecycleScope.launchWhenResumed {

        block.invoke(this)
    }.apply {

        nameAndJob[actionName] = this
    }
}