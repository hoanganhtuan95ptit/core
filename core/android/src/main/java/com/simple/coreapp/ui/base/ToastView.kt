package com.simple.coreapp.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.simple.core.utils.extentions.asObjectOrNull
import com.simple.state.ResultState

interface ToastView {

    fun showToast(message: String, state: ResultState<*>) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show()
    }

    private fun getActivity() = this.asObjectOrNull<FragmentActivity>()
        ?: this.asObjectOrNull<Fragment>()?.activity
        ?: error("not found")
}