package com.simple.coreapp.ui.base.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.simple.binding.findBinding
import com.simple.coreapp.utils.autoCleared

abstract class BaseViewBindingDialogFragment<T : ViewBinding>(@LayoutRes override val contentLayoutId: Int = 0) : BaseDialogFragment(contentLayoutId) {

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