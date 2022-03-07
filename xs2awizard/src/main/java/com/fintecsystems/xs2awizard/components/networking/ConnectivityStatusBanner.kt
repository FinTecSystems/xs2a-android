package com.fintecsystems.xs2awizard.components.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun ConnectivityStatusBanner() {
    var connectionState by remember { mutableStateOf(ConnectionState.UNKNOWN) }

    val context = LocalContext.current

    DisposableEffect(null) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectionState = ConnectionState.CONNECTED
            }

            override fun onLost(network: Network) {
                connectionState = ConnectionState.DISCONNECTED
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    val isConnected = connectionState === ConnectionState.CONNECTED

    if (isConnected) {
        Text(text = "Connected")
    } else {
        Text(text = "Disconnected")
    }
}