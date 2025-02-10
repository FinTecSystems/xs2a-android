package com.fintecsystems.xs2awizard.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.networking.encryption.Encryptor

private const val TAG = "NetworkingInstance"

/**
 * Singleton for everything network related.
 */
class NetworkingInstance private constructor(
    private val context: Context
) {
    private val requestQueue = Volley.newRequestQueue(context)
    private val rsaEncryptor = generatePublicKey()

    private var isConnected = false
    private var offlineRequests = mutableListOf<Request<*>>()
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isConnected = true

            offlineRequests.forEach { requestQueue.add(it) }
            offlineRequests.clear()
        }

        override fun onLost(network: Network) {
            isConnected = false
        }
    }

    var backendURL: String? = null
    var sessionKey: String? = null

    private fun generatePublicKey(): Encryptor {
        val modulus = context.getString(R.string.networking_public_key_modulus)
        val exponent = context.getString(R.string.networking_public_key_exponent)

        return Encryptor(modulus, exponent)
    }

    init {
        context.registerNetworkCallback(networkCallback)
    }

    fun finalize() {
        context.unregisterNetworkCallback(networkCallback)
    }

    fun encodeAndSendMessage(
        message: String,
        onSuccess: (String) -> Unit = ::defaultOnSuccess,
        onError: (VolleyError) -> Unit = ::defaultOnError,
    ) {
        val request = UrlEncodedRequest(Request.Method.POST,
            backendURL ?: context.getString(R.string.networking_backend_url),
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

    private fun constructBody(message: String) = mapOf(
        Pair(
            "data",
            rsaEncryptor.encodeMessage(message)
        ),
        Pair("key", sessionKey!!),
    )

    private fun defaultOnSuccess(res: String) {
        Log.d(TAG, "encodeAndSendMessage: Success! $res")
    }

    private fun defaultOnError(err: VolleyError) {
        Log.d(TAG, "encodeAndSendMessage: Error! $err")
    }

    companion object : SingletonHolder<NetworkingInstance, Context>(::NetworkingInstance)
}
