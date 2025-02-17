package com.simple.coreapp.utils.ext

import android.os.Parcelable
import com.simple.coreapp.ui.view.round.Background
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class ButtonInfo(
    val text: CharSequence,
    val background: Background? = null
) : Parcelable