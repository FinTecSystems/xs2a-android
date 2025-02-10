package com.fintecsystems.xs2awizard.networking.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

/**
 * Registers [networkCallback] to the ConnectivityManager to be informed of the current
 * network status.
 */
fun Context.registerNetworkCallback(networkCallback: ConnectivityManager.NetworkCallback) {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    } else {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
}

/**
 * Unregisters [networkCallback] from the ConnectivityManager.
 */
fun Context.unregisterNetworkCallback(networkCallback: ConnectivityManager.NetworkCallback) {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    connectivityManager.unregisterNetworkCallback(networkCallback)
}
