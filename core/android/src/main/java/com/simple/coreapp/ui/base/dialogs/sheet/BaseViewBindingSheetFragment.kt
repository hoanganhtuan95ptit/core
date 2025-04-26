package com.simple.coreapp.ui.base.dialogs.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.simple.binding.findBinding
import com.simple.coreapp.utils.autoCleared

abstract class BaseViewBindingSheetFragment<VB : ViewBinding>() : BaseSheetFragment() {

    var binding by autoCleared<VB>()

    protected open fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB {

        return findBinding(inflater.cloneInContext(requireActivity()), container)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = createBinding(inflater = inflater, container = container)

        return binding!!.root
    }
}