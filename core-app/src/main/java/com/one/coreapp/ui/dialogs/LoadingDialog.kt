package com.one.coreapp.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.SparseArray
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.StringRes
import com.one.coreapp.R
import com.one.coreapp.utils.extentions.toPx
import java.lang.ref.WeakReference
import kotlin.math.min

class LoadingDialog(context: Context) : Dialog(context) {

    private var id: Int? = null
    private var textResId = R.string.core_loading
    private val screenWidthPercent = 0.85F

    companion object {

        @JvmStatic
        private val cache = SparseArray<WeakReference<LoadingDialog>>()

        @JvmStatic
        fun getDialog(
            context: Context,
            @StringRes textResId: Int = R.string.core_loading
        ): LoadingDialog {
            val id = context.hashCode()
            var dialog = cache[id]?.get()
            if (dialog == null) {
                dialog = LoadingDialog(context)
                dialog.id = id
                dialog.textResId = textResId
                cache.put(id, WeakReference(dialog))
            }
            return dialog
        }

        @JvmStatic
        fun dismiss(context: Context) {
            cache[context.hashCode()]?.get()?.dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        id?.let { cache.remove(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        window?.setBackgroundDrawableResource(android.R.color.transparent)

        setContentView(R.layout.dialog_loading)

        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)

        val width = min((size.x * screenWidthPercent).toInt(), 400.toPx())

        window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)

        findViewById<TextView>(R.id.tvTitle).setText(textResId)
    }

}