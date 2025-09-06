package com.simple.deeplink.utils.exts

import android.content.ComponentCallbacks
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

internal fun ComponentCallbacks.getLifecycleOwner(): LifecycleOwner? = (this as? ComponentActivity) ?: (this as? Fragment)