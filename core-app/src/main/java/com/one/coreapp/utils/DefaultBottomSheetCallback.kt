package com.one.coreapp.utils

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

open class DefaultBottomSheetCallback: BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
    }
}