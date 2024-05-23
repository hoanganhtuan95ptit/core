package com.simple.coreapp.ui.base.activities

import androidx.appcompat.app.AppCompatActivity
import com.simple.coreapp.ui.base.ConfirmView
import com.simple.coreapp.ui.base.ToastView
import com.simple.coreapp.utils.JobQueue

open class BaseActivity : AppCompatActivity(), ToastView, ConfirmView {

    override var popupQueue = JobQueue()

    override fun onDestroy() {
        super.onDestroy()
        popupQueue.cancel()
    }
}