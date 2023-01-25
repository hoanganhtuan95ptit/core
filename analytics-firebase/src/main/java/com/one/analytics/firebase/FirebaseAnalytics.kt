package com.one.analytics.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.one.coreapp.App
import com.one.coreapp.utils.extentions.Analytics

class FirebaseAnalytics : Analytics {

    override fun logEvent(eventName: String, bundle: Bundle) {

        FirebaseAnalytics.getInstance(App.shared).logEvent(eventName, bundle)
    }

    override fun logException(throwable: Throwable) {

        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}