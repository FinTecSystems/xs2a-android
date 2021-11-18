package com.fintecsystems.xs2awizard.components.webview

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.util.Log
import android.webkit.*
import android.widget.*
import androidx.core.net.toUri
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.webview.XS2AJavascriptInterface.XS2AJavascriptInterfaceCallback
import com.fintecsystems.xs2awizard.helper.Utils

/**
 * WebView with URL-Bar on the top.
 */
class URLBarWebView(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
) : RelativeLayout(context, attrs, defStyle),
    XS2AJavascriptInterfaceCallback {
    val webView: WebView
    private val urlBarTextView: TextView
    private val secureIconImageView: ImageView

    lateinit var xS2AWizard: XS2AWizard

    private var currentUrl: String? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        inflate(context, R.layout.view_bar_url_webview, this).apply {
            webView = findViewById(R.id.webview)
            secureIconImageView = findViewById(R.id.secureIcon)

            findViewById<ImageButton>(R.id.closeButton).setOnClickListener { xS2AWizard.closeWebView() }

            urlBarTextView = findViewById(R.id.urlBarTextView)

            urlBarTextView.setOnLongClickListener {
                Utils.setClipboardText(context, currentUrl!!)

                true
            }

            webView.apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                addJavascriptInterface(XS2AJavascriptInterface(this@URLBarWebView), "App")

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        view.loadUrl(url)
                        return true
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)

                        currentUrl = url
                        urlBarTextView.text = url?.toUri()?.host ?: url
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        updateSecureIcon(view?.certificate != null)

                        // Page has finished loading, we can hide the loadingIndicator now.
                        xS2AWizard.decrementLoadingIndicatorLock()
                    }
                }
            }
        }
    }

    private fun updateSecureIcon(isSecure: Boolean) {
        val iconResId = if (isSecure) R.drawable.ic_ssl_secure else R.drawable.ic_ssl_unsecure

        secureIconImageView.setImageResource(iconResId)
    }

    fun hide() {
        visibility = GONE
    }

    fun show() {
        visibility = VISIBLE
    }

    override fun xS2AJavascriptInterfaceCallbackHandler(success: Boolean) {
        xS2AWizard.view?.post {
            xS2AWizard.closeWebView()

            if (success)
                xS2AWizard.submitForm("post-code")
        }
    }
}