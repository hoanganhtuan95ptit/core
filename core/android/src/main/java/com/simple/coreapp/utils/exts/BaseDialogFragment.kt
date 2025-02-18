package com.simple.coreapp.utils.exts

import androidx.fragment.app.FragmentManager
import com.simple.coreapp.ui.base.dialogs.BaseDialogFragment
import com.simple.coreapp.ui.base.dialogs.OnDismissListener
import com.simple.coreapp.ui.base.dialogs.sheet.BaseSheetFragment
import com.simple.coreapp.utils.extentions.isActive
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.firstOrNull

suspend fun BaseDialogFragment.showOrAwaitDismiss(fragmentManager: FragmentManager, tag: String) = channelFlow {

    onDismissListener = object : OnDismissListener {

        override fun onDismiss() {
            trySend(Unit)
        }
    }

    if (isActive()) kotlin.runCatching{

        show(fragmentManager, tag)
    }

    awaitClose {

        dismiss()
    }
}.firstOrNull()


suspend fun BaseSheetFragment.showOrAwaitDismiss(fragmentManager: FragmentManager, tag: String) = channelFlow {

    onDismissListener = object : OnDismissListener {

        override fun onDismiss() {
            trySend(Unit)
        }
    }

    if (isActive()) kotlin.runCatching {

        show(fragmentManager, tag)
    }

    awaitClose {

        dismiss()
    }
}.firstOrNull()