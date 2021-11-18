package com.fintecsystems.xs2awizard.components.webview

import android.webkit.JavascriptInterface

/**
 * JavaScript interface used to catch callback methods of redirects.
 */
class XS2AJavascriptInterface (
    private val callbackHandler: XS2AJavascriptInterfaceCallback
) {
    @JavascriptInterface
    fun successCallback() {
        callbackHandler.xS2AJavascriptInterfaceCallbackHandler(true)
    }

    @JavascriptInterface
    fun abortCallback() {
        callbackHandler.xS2AJavascriptInterfaceCallbackHandler(false)
    }

    interface XS2AJavascriptInterfaceCallback {
        fun xS2AJavascriptInterfaceCallbackHandler(success: Boolean)
    }
}
