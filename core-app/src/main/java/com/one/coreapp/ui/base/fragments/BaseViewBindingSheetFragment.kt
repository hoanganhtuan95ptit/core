package com.one.coreapp.ui.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.one.coreapp.utils.autoCleared
import com.one.coreapp.utils.extentions.findBinding

abstract class BaseViewBindingSheetFragment<T : ViewBinding>(@LayoutRes override val contentLayoutId: Int = 0) : BaseSheetFragment(contentLayoutId) {

    var binding by autoCleared<T>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return if (contentLayoutId > 0) {

            super.onCreateView(inflater.cloneInContext(requireActivity()), container, savedInstanceState)
        } else {

            binding = findBinding(inflater.cloneInContext(requireActivity()), container)
            binding!!.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = binding ?: findBinding(view)
    }
}