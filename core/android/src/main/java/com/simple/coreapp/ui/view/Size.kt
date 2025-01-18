package com.simple.coreapp.ui.view

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.simple.coreapp.utils.ext.updateMargin

data class Size(
    val width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
)

data class Margin(
    val top: Int = 0,
    val bottom: Int = 0,
    val left: Int = 0,
    val right: Int = 0
)

data class Padding(
    val top: Int = 0,
    val bottom: Int = 0,
    val left: Int = 0,
    val right: Int = 0
)

fun View.setSize(size: Size? = null) {

    size ?: return

    updateLayoutParams {
        width = size.width
        height = size.height
    }
}

fun View.setMargin(margin: Margin? = null) {

    margin ?: return

    updateMargin(
        left = margin.left,
        top = margin.top,
        right = margin.right,
        bottom = margin.bottom
    )
}

fun View.setPadding(padding: Padding? = null) {

    padding ?: return

    updatePadding(
        left = padding.left,
        top = padding.top,
        right = padding.right,
        bottom = padding.bottom
    )
}
