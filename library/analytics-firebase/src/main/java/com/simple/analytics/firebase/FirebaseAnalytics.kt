package com.simple.analytics.firebase

import android.annotation.SuppressLint
import androidx.core.os.bundleOf
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.simple.analytics.Analytics

class FirebaseAnalytics : Analytics {

    @SuppressLint("MissingPermission")
    override suspend fun execute(eventName: String, vararg params: Pair<String, String>) {

        Firebase.analytics.logEvent(eventName, bundleOf(*params))
    }
}