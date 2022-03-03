package com.fintecsystems.xs2awizard.helper.networking

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

// https://medium.com/scalereal/observing-live-connectivity-status-in-jetpack-compose-way-f849ce8431c7

/**
 * Network Utility to observe availability or unavailability of Internet connection
 */
@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(ConnectionState.CONNECTED)
        }

        override fun onLost(network: Network) {
            trySend(ConnectionState.DISCONNECTED)
        }
    }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    // Set current state
    trySend(ConnectionState.UNKNOWN)

    // Remove callback when not used
    awaitClose {
        // Remove listeners
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

/**
 * Current Network-Connectivity-State as State.
 */
@ExperimentalCoroutinesApi
@Composable
fun connectivityState() = with(LocalContext.current) {
    observeConnectivityAsFlow().collectAsState(initial = ConnectionState.UNKNOWN)
}