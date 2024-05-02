package com.simple.coreapp.utils.extentions

import android.os.Build
import android.view.WindowInsets

internal val STATUS_BAR_DEFAULT by lazy {
    20.toPx()
}

internal val NAVIGATION_BAR_DEFAULT by lazy {
    20.toPx()
}

fun WindowInsets.getStatusBar() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    getInsets(WindowInsets.Type.systemBars()).top
} else {
    systemWindowInsetTop
}

fun WindowInsets.getNavigationBar() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    getInsets(WindowInsets.Type.navigationBars()).bottom
} else {
    systemWindowInsetBottom
}

fun WindowInsets.getHeightStatusBarOrNull() = getStatusBar().takeIf { it > STATUS_BAR_DEFAULT }

fun WindowInsets.getHeightNavigationBarOrNull() = getNavigationBar().takeIf { it > NAVIGATION_BAR_DEFAULT }