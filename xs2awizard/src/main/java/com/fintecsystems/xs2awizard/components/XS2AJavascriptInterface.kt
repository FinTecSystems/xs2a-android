package com.fintecsystems.xs2awizard.components

import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.LinearLayout
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.XS2AWizard

/**
 * JavaScript interface used to catch callback methods of redirects.
 */
class XS2AJavascriptInterface (
    private val wizard: XS2AWizard
) {
    private fun closeWebView() {
        wizard.view?.findViewById<WebView>(R.id.webview)?.apply {
            loadUrl("about:blank")
            visibility = View.GONE
        }
        wizard.view?.findViewById<LinearLayout>(R.id.form_container)?.apply {
            visibility = View.VISIBLE
        }
    }

    @JavascriptInterface
    fun successCallback() {
        wizard.view?.post {
            closeWebView()

            wizard.submitForm("post-code")
        }
    }

    @JavascriptInterface
    fun abortCallback() {
        wizard.view?.post {
            closeWebView()
        }
    }
}
