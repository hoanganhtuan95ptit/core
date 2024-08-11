package com.simple.crashlytics.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.simple.crashlytics.Crashlytics

class FirebaseCrashlytics(private val firebaseApp: FirebaseApp) : Crashlytics {

    private val firebaseCrashlytics by lazy {

        firebaseApp.get(FirebaseCrashlytics::class.java) as FirebaseCrashlytics
    }

    override suspend fun execute(eventName: String, throwable: Throwable, vararg params: Pair<String, String>) {

        val map = hashMapOf("eventName" to eventName, *params)

        firebaseCrashlytics.recordException(RuntimeException(map.toString(), throwable))
    }
}