package com.simple.coreapp.ui.base.activities

import androidx.appcompat.app.AppCompatActivity
import com.simple.coreapp.ui.base.ConfirmView
import com.simple.coreapp.ui.base.TagView
import com.simple.coreapp.ui.base.ToastView

open class BaseActivity : AppCompatActivity(), TagView, ToastView, ConfirmView {

    override var mCleared: Boolean = false

    override val mBagOfTags: MutableMap<String, Any> = hashMapOf()

    override fun onDestroy() {
        super.onDestroy()
        tagClear()
    }

}