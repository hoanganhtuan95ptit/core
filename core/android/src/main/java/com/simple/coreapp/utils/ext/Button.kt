package com.simple.coreapp.utils.ext

import android.os.Parcelable
import com.simple.coreapp.ui.view.Background
import kotlinx.parcelize.Parcelize

@Parcelize
data class ButtonInfo(
    val text: CharSequence,
    val background: Background? = null
) : Parcelable