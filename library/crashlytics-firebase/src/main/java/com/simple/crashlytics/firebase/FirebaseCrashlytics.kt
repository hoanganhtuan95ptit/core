package com.simple.crashlytics.firebase

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.simple.crashlytics.Crashlytics

class FirebaseCrashlytics : Crashlytics {

    override suspend fun execute(eventName: String, throwable: Throwable, vararg params: Pair<String, String>) {

        val map = hashMapOf("eventName" to eventName, *params)

        Firebase.crashlytics.recordException(RuntimeException(map.toString(), throwable))
    }
}