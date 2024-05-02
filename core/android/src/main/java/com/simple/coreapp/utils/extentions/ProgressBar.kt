package com.simple.coreapp.utils.extentions

import android.animation.PropertyValuesHolder
import android.widget.ProgressBar

fun ProgressBar.setProcessAnim(progressNew:Int){
    listOf( PropertyValuesHolder.ofInt("progress", progress, progressNew)).animation(onUpdate = {
        progress = it.getAnimatedValue("progress") as Int
    })
}