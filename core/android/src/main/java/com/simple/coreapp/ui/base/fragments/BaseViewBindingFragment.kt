package com.simple.coreapp.ui.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.simple.binding.findBinding

abstract class BaseViewBindingFragment<T : ViewBinding>(@LayoutRes val contentLayoutId: Int = 0) : BaseFragment(contentLayoutId) {

    var binding: T? = null

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return if (contentLayoutId > 0) {

            super.onCreateView(inflater.cloneInContext(requireActivity()), container, savedInstanceState)
        } else {

            binding = findBinding(inflater.cloneInContext(requireActivity()), container)
            binding!!.root
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = binding ?: findBinding(view)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}