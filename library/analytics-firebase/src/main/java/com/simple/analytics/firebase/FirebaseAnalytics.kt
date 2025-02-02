package com.simple.analytics.firebase

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.simple.analytics.Analytics

class FirebaseAnalytics(private val context: Context) : Analytics {

    @SuppressLint("MissingPermission")
    override suspend fun execute(eventName: String, vararg params: Pair<String, String>) {

        FirebaseAnalytics.getInstance(context).logEvent(eventName, bundleOf(*params))
    }
}