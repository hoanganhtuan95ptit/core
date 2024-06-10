package com.simple.coreapp.utils.ext

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.io.BufferedReader
import java.io.InputStreamReader


fun Context.isInternetAvailable(): Boolean = kotlin.runCatching {

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network)

    return if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {

        isInternetAvailableWithGoogle()
    } else {

        false
    }
}.getOrElse {

    false
}

fun isInternetAvailableWithGoogle(): Boolean = runCatching {

    val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 google.com")
    val reader = BufferedReader(InputStreamReader(process.inputStream))

    var count = 0
    while (reader.readLine() != null) {
        count++
    }

    reader.close()

    count > 0
}.getOrElse {

    false
}