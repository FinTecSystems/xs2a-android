package com.fintecsystems.xs2awizard.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.networking.encryption.Encryptor
import com.fintecsystems.xs2awizard.networking.utils.registerNetworkCallback
import com.fintecsystems.xs2awizard.networking.utils.unregisterNetworkCallback

/**
 * Singleton for everything network related.
 */
internal class NetworkingService(
    private val context: Context,
    private val sessionKey: String,
    private val backendURL: String
) : ConnectivityManager.NetworkCallback() {
    private val requestQueue = Volley.newRequestQueue(context)
    private val encryptor = Encryptor(
        context.getString(R.string.networking_public_key_modulus),
        context.getString(R.string.networking_public_key_exponent)
    )

    private var isConnected = false
    private var offlineRequests = mutableListOf<Request<*>>()

    init {
        context.registerNetworkCallback(this)
    }

    fun finalize() {
        context.unregisterNetworkCallback(this)
    }

    fun encodeAndSendMessage(
        message: String,
        onSuccess: (String) -> Unit,
        onError: (VolleyError) -> Unit,
    ) {
        val request = UrlEncodedRequest(Request.Method.POST,
            backendURL,
            constructBody(message),
            { onSuccess(it) },
            { onError(it) }
        )

        request.retryPolicy = DefaultRetryPolicy(
            180 * 1000, // 180s timeout
            Int.MAX_VALUE,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT,
        )

        if (isConnected) {
            requestQueue.add(request)
        } else {
            offlineRequests.add(request)
        }
    }

    override fun onAvailable(network: Network) {
        isConnected = true

        offlineRequests.forEach { requestQueue.add(it) }
        offlineRequests.clear()
    }

    override fun onLost(network: Network) {
        isConnected = false
    }

    private fun constructBody(message: String) = mapOf(
        Pair(
            "data",
            encryptor.encodeMessage(message)
        ),
        Pair("key", sessionKey),
    )
}
