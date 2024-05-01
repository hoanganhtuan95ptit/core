package com.simple.analytics.firebase

import android.content.Context
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.simple.analytics.Analytics

class FirebaseAnalytics(private val context: Context) : Analytics {

    override suspend fun execute(vararg params: Pair<String, String>) {

        FirebaseAnalytics.getInstance(context).logEvent(params.first().first, bundleOf(*params))
    }
}