package com.simple.coreapp.ui.base.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment(@LayoutRes open val contentLayoutId: Int = 0) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = if (contentLayoutId != 0) {

        inflater.cloneInContext(requireActivity()).inflate(contentLayoutId, container, false)
    } else {

        null
    }
}