package com.simple.coreapp.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.simple.core.utils.extentions.asObjectOrNull
import com.simple.coreapp.ui.base.dialogs.OnDismissListener
import com.simple.coreapp.ui.dialogs.confirm.HorizontalConfirmDialogFragment
import com.simple.coreapp.ui.dialogs.confirm.VerticalConfirmDialogFragment
import com.simple.coreapp.utils.JobQueue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull

interface ConfirmView {

    fun showConfirm(
        isCancel: Boolean = true,
        isVertical: Boolean = true,

        keyRequestPositive: String? = null,
        keyRequestNegative: String? = null,

        image: Int? = null,

        title: String? = null,
        message: String? = null,

        negative: String? = null,
        positive: String? = null
    ) {

        getActivity().asObjectOrNull<TagView>()?.setTagIfAbsent("CONFIRM_VIEW", JobQueue())?.submit {

            awaitConfirm(
                isCancel = isCancel,
                isVertical = isVertical,
                keyRequestPositive = keyRequestPositive,
                keyRequestNegative = keyRequestNegative,
                image = image,
                title = title,
                message = message,
                negative = negative,
                positive = positive
            )
        }
    }

    suspend fun awaitConfirm(
        fragmentManager: FragmentManager? = null,

        isCancel: Boolean = true,
        isVertical: Boolean = true,

        keyRequestPositive: String? = null,
        keyRequestNegative: String? = null,

        image: Int? = null,

        title: String? = null,
        message: String? = null,

        negative: String? = null,
        positive: String? = null
    ) = channelFlow {

        val dialog = if (isVertical) {
            VerticalConfirmDialogFragment.newInstance(isCancel, keyRequestPositive, keyRequestNegative, image, title, message, negative, positive)
        } else {
            HorizontalConfirmDialogFragment.newInstance(isCancel, keyRequestPositive, keyRequestNegative, image, title, message, negative, positive)
        }

        dialog.onDismissListener = object : OnDismissListener {

            override fun onDismiss() {
                trySend(Unit)
            }
        }

        val fragmentManagerWrap = fragmentManager ?: getFragmentManager()

        dialog.show(fragmentManagerWrap, keyRequestPositive ?: keyRequestNegative ?: "")

        awaitClose {

        }
    }.firstOrNull()

    private fun getFragmentManager() = this.asObjectOrNull<FragmentActivity>()?.supportFragmentManager
        ?: this.asObjectOrNull<Fragment>()?.childFragmentManager
        ?: error("not found")

    private fun getActivity() = this.asObjectOrNull<FragmentActivity>()
        ?: this.asObjectOrNull<Fragment>()?.activity
        ?: error("not found")
}