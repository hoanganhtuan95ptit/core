package com.simple.coreapp.ui.base.activities

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.simple.core.utils.extentions.findGenericClassBySuperClass
import com.simple.coreapp.utils.ext.getViewModel
import org.koin.core.parameter.ParametersDefinition

abstract class BaseViewModelActivity<T : ViewBinding, VM : ViewModel> : BaseViewBindingActivity<T>() {

    open val viewModel: VM by lazy {
        getViewModel(this, findGenericClassBySuperClass(ViewModel::class.java)!!, parameters = getParameter())
    }

    protected open fun getParameter(): ParametersDefinition? {
        return null
    }
}