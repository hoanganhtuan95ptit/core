package com.simple.coreapp.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.simple.core.utils.extentions.asObjectOrNull
import com.simple.coreapp.entities.ToastType
import com.simple.coreapp.ui.base.dialogs.OnDismissListener
import com.simple.coreapp.ui.dialogs.ToastDialog
import com.simple.coreapp.utils.JobQueue
import com.simple.state.ResultState
import com.simple.state.isFailed
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull

interface ToastView {

    fun showToast(message: String, state: ResultState<*>) {

        val activity = getActivity().asObjectOrNull<ToastView>() ?: return

        activity.showToast(type = if (state.isFailed()) ToastType.ERROR else ToastType.SUCCESS, message = message)
    }

    fun showToast(
        type: ToastType,

        image: Int? = null,
        message: String? = null,
    ) {

        this.asObjectOrNull<TagView>()?.setTagIfAbsent("TOAST_VIEW", JobQueue())?.submit {

            awaitToast(
                type = type,
                image = image,
                message = message,
            )
        }
    }

    suspend fun awaitToast(
        fragmentManager: FragmentManager? = null,

        type: ToastType,

        image: Int? = null,
        message: String? = null,
    ) = channelFlow {

        val dialog = ToastDialog.newInstance(type, image, message)

        dialog.onDismissListener = object : OnDismissListener {

            override fun onDismiss() {
                trySend(Unit)
            }
        }

        val fragmentManagerWrap = fragmentManager ?: getFragmentManager()

        dialog.show(fragmentManagerWrap, "")

        awaitClose {
            dialog.dismiss()
        }
    }.firstOrNull()

    private fun getFragmentManager() = this.asObjectOrNull<FragmentActivity>()?.supportFragmentManager
        ?: this.asObjectOrNull<Fragment>()?.childFragmentManager
        ?: error("not found")

    private fun getActivity() = this.asObjectOrNull<FragmentActivity>()
        ?: this.asObjectOrNull<Fragment>()?.activity
        ?: error("not found")
}