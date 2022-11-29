package com.one.coreapp.ui.base.activities

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.one.coreapp.utils.extentions.findBinding

abstract class BaseViewBindingActivity<T : ViewBinding> : BaseActivity() {

    var binding: ViewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = findBinding(layoutInflater)

        setContentView(binding!!.root)
    }
}