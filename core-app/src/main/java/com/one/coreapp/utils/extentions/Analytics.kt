package com.one.coreapp.utils.extentions

import android.os.Bundle

interface Analytics {

    fun logEvent(eventName: String, bundle: Bundle)

    fun logException(throwable: Throwable)
}