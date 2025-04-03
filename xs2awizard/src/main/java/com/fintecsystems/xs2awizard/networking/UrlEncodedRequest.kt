package com.fintecsystems.xs2awizard.networking

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

/**
 * A simple [StringRequest] subclass.
 * Uses x-www-form-urlencoded bodies.
 */
internal class UrlEncodedRequest(
    requestMethod: Int = Method.GET,
    requestUrl: String,
    private val params: Map<String, String>,
    responseListener: Response.Listener<String>,
    responseErrorListener: Response.ErrorListener
) : StringRequest(requestMethod, requestUrl, responseListener, responseErrorListener) {
    override fun getBodyContentType() = "application/x-www-form-urlencoded"

    override fun getHeaders() = mapOf(
        Pair("Content-Type", "application/x-www-form-urlencoded"),
        Pair("Accept", "application/json")
    )

    override fun getParams() = params
}
