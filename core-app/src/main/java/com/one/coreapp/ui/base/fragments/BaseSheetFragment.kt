package com.one.coreapp.ui.base.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.doOnLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.one.coreapp.R
import com.one.coreapp.ui.ComponentCache
import com.one.coreapp.utils.extentions.getColorFromAttr
import com.one.coreapp.utils.extentions.getNavigationBarHeight

abstract class BaseSheetFragment(@LayoutRes open val contentLayoutId: Int = 0) : BottomSheetDialogFragment(), BackPressedView , ComponentCache {

    override val mBagOfTags: HashMap<String, Any> = hashMapOf()

    protected lateinit var bottomSheet: View

    protected lateinit var behavior: BottomSheetBehavior<*>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {

            val activity = activity ?: return@setOnShowListener

            val windowDialog = dialog.window ?: return@setOnShowListener

            val windowActivity = activity.window ?: return@setOnShowListener

            windowDialog.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, windowActivity.decorView.bottom + getNavigationBarHeight(activity))
        }

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = if (contentLayoutId != 0) {

        inflater.cloneInContext(requireActivity()).inflate(contentLayoutId, container, false)
    } else {

        null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dialog?.window?.navigationBarColor = view.context.getColorFromAttr(R.attr.colorNavigationBar)

        super.onViewCreated(view, savedInstanceState)

        bottomSheet = (dialog as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
        behavior = BottomSheetBehavior.from(bottomSheet)

        view.doOnLayout {
            (bottomSheet.parent as View).setBackgroundColor(Color.TRANSPARENT)
            bottomSheet.setBackgroundResource(android.R.color.transparent)
        }
    }
}