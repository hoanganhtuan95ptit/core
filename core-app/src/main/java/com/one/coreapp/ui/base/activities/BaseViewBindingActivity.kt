package com.one.coreapp.ui.base.activities

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.one.coreapp.utils.extentions.findBinding

abstract class BaseViewBindingActivity<T : ViewBinding> : BaseActivity() {

    var binding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = findBinding(layoutInflater)

        setContentView(binding!!.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}