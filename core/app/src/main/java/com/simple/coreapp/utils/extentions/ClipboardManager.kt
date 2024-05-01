package com.simple.coreapp.utils.extentions

import android.content.ClipboardManager
import android.os.Build

fun ClipboardManager.clear() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        clearPrimaryClip()
    } else {
        text = ""
    }
}

fun ClipboardManager.text(): CharSequence? {

    return primaryClip?.getItemAt(0)?.text
}


fun ClipboardManager.haveText(): Boolean {

    return hasPrimaryClip()
}

