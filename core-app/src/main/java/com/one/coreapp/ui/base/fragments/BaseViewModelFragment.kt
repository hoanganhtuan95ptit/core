package com.one.coreapp.ui.base.fragments

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.one.coreapp.ui.fragment.base.BaseTransitionFragment
import com.one.coreapp.utils.extentions.findGenericClassBySuperClass
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition

abstract class BaseViewModelFragment<T : ViewBinding, VM : ViewModel>(@LayoutRes contentLayoutId: Int = 0) : BaseTransitionFragment<T>(contentLayoutId) {

    open val viewModel: VM by lazy {
        getKoin().getViewModel(this, findGenericClassBySuperClass(ViewModel::class.java)!!, parameters = getParameter())
    }

    protected open fun getParameter(): ParametersDefinition? {
        return null
    }

}