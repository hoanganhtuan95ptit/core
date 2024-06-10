package com.simple.coreapp.ui.base.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.simple.coreapp.R
import com.simple.coreapp.ui.base.ConfirmView
import com.simple.coreapp.ui.base.TagView
import com.simple.coreapp.ui.base.ToastView
import com.simple.coreapp.utils.extentions.getColorFromAttr


open class BaseFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId), TagView, ToastView, ConfirmView {

    override var mCleared: Boolean = false

    override val mBagOfTags: MutableMap<String, Any> = hashMapOf()

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().window.navigationBarColor = view.context.getColorFromAttr(R.attr.colorNavigationBar)

        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        tagClear()
    }
}