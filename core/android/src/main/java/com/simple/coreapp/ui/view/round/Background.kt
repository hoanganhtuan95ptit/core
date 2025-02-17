package com.simple.coreapp.ui.view.round

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Background(
    var backgroundColor: Int? = null,

    val cornerRadius: Int? = null,
    val cornerRadius_TL: Int? = null,
    val cornerRadius_TR: Int? = null,
    val cornerRadius_BL: Int? = null,
    val cornerRadius_BR: Int? = null,

    val strokeWidth: Int? = null,
    val strokeColor: Int? = null,
    val strokeDashGap: Int? = null,
    val strokeDashWidth: Int? = null
) : Parcelable


fun RoundViewDelegate.setBackground(background: Background? = null) {

    background ?: return

    background.backgroundColor?.let {
        this.backgroundColor = it
    }

    background.cornerRadius?.let {
        this.cornerRadius = it
    }

    background.cornerRadius_TL?.let {
        this.cornerRadius_TL = it
    }

    background.cornerRadius_TR?.let {
        this.cornerRadius_TR = it
    }

    background.cornerRadius_BL?.let {
        this.cornerRadius_BL = it
    }

    background.cornerRadius_BR?.let {
        this.cornerRadius_BR = it
    }

    background.strokeWidth?.let {
        this.strokeWidth = it
    }

    background.strokeColor?.let {
        this.strokeColor = it
    }

    background.strokeDashGap?.let {
        this.strokeDashGap = it
    }

    background.strokeDashWidth?.let {
        this.strokeDashWidth = it
    }

    setBgSelector()
}