package com.one.coreapp.ui.base.fragments

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.one.coreapp.utils.extentions.findGenericClassBySuperClass
import com.one.coreapp.utils.extentions.getViewModel
import org.koin.core.parameter.ParametersDefinition

abstract class BaseViewModelSheetFragment<T : ViewBinding, VM : ViewModel>(@LayoutRes override val contentLayoutId: Int = 0) : BaseViewBindingSheetFragment<T>(contentLayoutId) {

    protected open val viewModel: VM by lazy {
        getViewModel(this, findGenericClassBySuperClass(ViewModel::class.java)!!, parameters = getParameter())
    }

    protected open fun getParameter(): ParametersDefinition? {
        return null
    }
}