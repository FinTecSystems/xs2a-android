package com.fintecsystems.xs2awizard.components.webview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.webview.XS2AJavascriptInterface.XS2AJavascriptInterfaceCallback

/**
 * WebView with URL-Bar on the top.
 */
class URLBarWebView(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
): RelativeLayout(context, attrs, defStyle),
    XS2AJavascriptInterfaceCallback {
    val webView: WebView
    private val urlBarTextView: TextView

    lateinit var xS2AWizard: XS2AWizard

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        inflate(context, R.layout.view_bar_url_webview, this).apply {
            webView = findViewById(R.id.webview)
            urlBarTextView = findViewById(R.id.urlBarTextView)

            urlBarTextView.setOnLongClickListener {
                setClipboardText(urlBarTextView.text as String)

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
                        urlBarTextView.text = url
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        // Page has finished loading, we can hide the loadingIndicator now.
                        xS2AWizard.decrementLoadingIndicatorLock()
                    }
                }
            }
        }
    }

    fun hide() {
        visibility = GONE
    }

    fun show() {
        visibility = VISIBLE
    }

    fun setClipboardText(text: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, "Copied text!", Toast.LENGTH_SHORT).show()
    }

    override fun xS2AJavascriptInterfaceCallbackHandler(success: Boolean) {
        xS2AWizard.view?.post {
            xS2AWizard.closeWebView()

            if (success)
                xS2AWizard.submitForm("post-code")
        }
    }
}