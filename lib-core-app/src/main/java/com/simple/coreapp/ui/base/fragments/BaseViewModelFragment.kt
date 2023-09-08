package com.simple.coreapp.ui.base.fragments

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.simple.coreapp.utils.extentions.findGenericClassBySuperClass
import com.simple.coreapp.utils.extentions.getViewModel
import org.koin.core.parameter.ParametersDefinition

abstract class BaseViewModelFragment<T : ViewBinding, VM : ViewModel>(@LayoutRes contentLayoutId: Int = 0) : BaseTransitionFragment<T>(contentLayoutId) {

    open val viewModel: VM by lazy {
        getViewModel(this, findGenericClassBySuperClass(ViewModel::class.java)!!, parameters = getParameter())
    }

    protected open fun getParameter(): ParametersDefinition? {
        return null
    }

}