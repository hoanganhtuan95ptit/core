package com.simple.coreapp.ui.base.dialogs.sheet

import android.content.DialogInterface
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
import com.simple.coreapp.ui.base.dialogs.OnDismissListener
import com.simple.coreapp.utils.autoCleared

abstract class BaseSheetFragment(@LayoutRes open val contentLayoutId: Int = 0) : BottomSheetDialogFragment() {

    protected var container by autoCleared<ViewGroup>()
    protected var bottomSheet by autoCleared<ViewGroup>()
    protected var coordinator by autoCleared<ViewGroup>()


    protected var behavior by autoCleared<BottomSheetBehavior<*>>()


    var onDismissListener: OnDismissListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = if (contentLayoutId != 0) {

        inflater.cloneInContext(requireActivity()).inflate(contentLayoutId, container, false)
    } else {

        null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        val dialog = (dialog as? BottomSheetDialog) ?: return

        this.behavior = dialog.behavior

        this.container = container ?: dialog.findViewById(com.google.android.material.R.id.container)!!
        this.coordinator = coordinator ?: dialog.findViewById(com.google.android.material.R.id.coordinator)!!
        this.bottomSheet = bottomSheet ?: dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!


        view.doOnLayout {

            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            coordinator?.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDismissListener = null
    }
}