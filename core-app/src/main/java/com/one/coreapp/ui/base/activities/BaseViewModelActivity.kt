package com.one.coreapp.ui.base.activities

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.one.coreapp.utils.extentions.findGenericClassBySuperClass
import com.one.coreapp.utils.extentions.getViewModel
import org.koin.core.parameter.ParametersDefinition

abstract class BaseViewModelActivity<T : ViewBinding, VM : ViewModel> : BaseViewBindingActivity<T>() {

    open val viewModel: VM by lazy {
        getViewModel(this, findGenericClassBySuperClass(ViewModel::class.java)!!, parameters = getParameter())
    }

    protected open fun getParameter(): ParametersDefinition? {
        return null
    }
}