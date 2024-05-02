package com.simple.coreapp.ui.base.dialogs

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.simple.core.utils.extentions.findGenericClassBySuperClass
import com.simple.coreapp.utils.ext.getViewModel
import org.koin.core.parameter.ParametersDefinition

abstract class BaseViewModelDialogFragment<T : ViewBinding, VM : ViewModel>(@LayoutRes override val contentLayoutId: Int = 0) : BaseViewBindingDialogFragment<T>(contentLayoutId) {

    protected open val viewModel: VM by lazy {
        getViewModel(this, findGenericClassBySuperClass(ViewModel::class.java)!!, parameters = getParameter())
    }

    protected open fun getParameter(): ParametersDefinition? {
        return null
    }
}