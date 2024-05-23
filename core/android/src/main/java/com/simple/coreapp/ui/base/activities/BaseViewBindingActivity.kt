package com.simple.coreapp.ui.base.activities

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.simple.binding.findBinding

abstract class BaseViewBindingActivity<T : ViewBinding> : BaseActivity() {

    var binding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        binding = findBinding(layoutInflater)

        setContentView(binding!!.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}