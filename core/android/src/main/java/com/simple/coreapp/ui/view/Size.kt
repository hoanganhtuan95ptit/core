package com.simple.coreapp.ui.view

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.simple.coreapp.ui.view.round.RoundViewDelegate
import com.simple.coreapp.utils.ext.updateMargin

data class Size(
    val width: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    val weight: Float = 1f
)

val DEFAULT_SIZE = Size()

fun View.setSize(size: Size? = null) {

    size ?: return

    updateLayoutParams {
        width = size.width
        height = size.height

        if (this is LinearLayout.LayoutParams) {
            weight = size.weight
        }
    }
}


data class Margin(
    val top: Int = 0,
    val bottom: Int = 0,
    val left: Int = 0,
    val right: Int = 0
)

val DEFAULT_MARGIN = Margin()

fun Margin(
    margin: Int = 0
) = Margin(
    top = margin,
    bottom = margin,
    left = margin,
    right = margin
)

fun Margin(
    marginVertical: Int = 0,
    marginHorizontal: Int = 0
) = Margin(
    top = marginVertical,
    bottom = marginVertical,
    left = marginHorizontal,
    right = marginHorizontal
)

fun View.setMargin(margin: Margin? = null) {

    margin ?: return

    updateMargin(
        left = margin.left,
        top = margin.top,
        right = margin.right,
        bottom = margin.bottom
    )
}


data class Padding(
    val top: Int = 0,
    val bottom: Int = 0,
    val left: Int = 0,
    val right: Int = 0
)

val DEFAULT_PADDING = Padding()

fun Padding(
    padding: Int = 0
) = Padding(
    top = padding,
    bottom = padding,
    left = padding,
    right = padding
)

fun Padding(
    paddingVertical: Int = 0,
    paddingHorizontal: Int = 0
) = Padding(
    top = paddingVertical,
    bottom = paddingVertical,
    left = paddingHorizontal,
    right = paddingHorizontal
)

fun View.setPadding(padding: Padding? = null) {

    padding ?: return

    updatePadding(
        left = padding.left,
        top = padding.top,
        right = padding.right,
        bottom = padding.bottom
    )
}

data class Background(
    var backgroundColor: Int = Color.TRANSPARENT,

    val cornerRadius_TL: Int = 0,
    val cornerRadius_TR: Int = 0,
    val cornerRadius_BL: Int = 0,
    val cornerRadius_BR: Int = 0,

    val strokeWidth: Int = 0,
    val strokeColor: Int = Color.TRANSPARENT,
    val strokeDashGap: Int = 0,
    val strokeDashWidth: Int = 0
)

val DEFAULT_BACKGROUND = Background()

fun Background(
    backgroundColor: Int = Color.TRANSPARENT,

    cornerRadius: Int = 0,

    strokeWidth: Int = 0,
    strokeColor: Int = Color.TRANSPARENT,
    strokeDashGap: Int = 0,
    strokeDashWidth: Int = 0
) = Background(
    backgroundColor = backgroundColor,

    cornerRadius_TL = cornerRadius,
    cornerRadius_TR = cornerRadius,
    cornerRadius_BL = cornerRadius,
    cornerRadius_BR = cornerRadius,

    strokeWidth = strokeWidth,
    strokeColor = strokeColor,
    strokeDashGap = strokeDashGap,
    strokeDashWidth = strokeDashWidth
)

fun RoundViewDelegate.setBackground(background: Background? = null) {

    background ?: return

    background.backgroundColor.let {
        this.backgroundColor = it
    }

    background.cornerRadius_TL.let {
        this.cornerRadius_TL = it
    }

    background.cornerRadius_TR.let {
        this.cornerRadius_TR = it
    }

    background.cornerRadius_BL.let {
        this.cornerRadius_BL = it
    }

    background.cornerRadius_BR.let {
        this.cornerRadius_BR = it
    }

    background.strokeWidth.let {
        this.strokeWidth = it
    }

    background.strokeColor.let {
        this.strokeColor = it
    }

    background.strokeDashGap.let {
        this.strokeDashGap = it
    }

    background.strokeDashWidth.let {
        this.strokeDashWidth = it
    }

    setBgSelector()
}
