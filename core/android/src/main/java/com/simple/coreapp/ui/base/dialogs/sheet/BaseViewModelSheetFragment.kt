package com.simple.coreapp.ui.base.dialogs.sheet

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.simple.core.utils.extentions.findGenericClassBySuperClass
import com.simple.coreapp.utils.ext.getViewModel
import org.koin.core.parameter.ParametersDefinition

abstract class BaseViewModelSheetFragment<T : ViewBinding, VM : ViewModel> : BaseViewBindingSheetFragment<T>() {

    protected open val viewModel: VM by lazy {
        getViewModel(this, findGenericClassBySuperClass(ViewModel::class.java)!!, parameters = getParameter())
    }

    protected open fun getParameter(): ParametersDefinition? {
        return null
    }
}