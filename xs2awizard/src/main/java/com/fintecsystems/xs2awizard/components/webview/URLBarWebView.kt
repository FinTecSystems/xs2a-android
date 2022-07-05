package com.fintecsystems.xs2awizard.components.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.webview.XS2AJavascriptInterface.XS2AJavascriptInterfaceCallback
import com.fintecsystems.xs2awizard.helper.Utils
import kotlinx.coroutines.launch

/**
 * Renders a WebView with a ToolBar.
 *
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun URLBarWebView(viewModel: XS2AWizardViewModel) {
    val targetUrl by viewModel.currentWebViewUrl.observeAsState(null)

    var loadingIndicatorProgress by remember { mutableStateOf(0) }

    var webView: WebView? = null
    var currentUrl by remember { mutableStateOf<String?>(null) }
    var hasCertificate by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val callbackHandler = object : XS2AJavascriptInterfaceCallback {
        override fun xS2AJavascriptInterfaceCallbackHandler(success: Boolean) {
            coroutineScope.launch {
                viewModel.closeWebView()

                if (success)
                    viewModel.submitForm("post-code")
            }
        }
    }

    DisposableEffect(targetUrl, webView) {
        webView?.loadUrl(targetUrl ?: "about:blank")

        onDispose { /* no-op */ }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                color = XS2ATheme.CURRENT.webViewBackgroundColor
            )
    ) {
        // Top Bar
        Column(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    modifier = Modifier.width(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = XS2ATheme.CURRENT.webViewBackgroundColor),
                    onClick = { viewModel.closeWebView() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.close_webview),
                        colorFilter = ColorFilter.tint(XS2ATheme.CURRENT.webViewIconColor)
                    )
                }

                Text(
                    text = currentUrl?.toUri()?.host ?: currentUrl ?: "",
                    fontSize = 15.sp,
                    maxLines = 1,
                    color = XS2ATheme.CURRENT.webViewTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                Utils.setClipboardText(context, currentUrl ?: "")
                            }
                        )
                    }
                )

                Image(
                    modifier = Modifier.width(48.dp),
                    painter = painterResource(
                        if (hasCertificate) R.drawable.ic_ssl_secure else R.drawable.ic_warning
                    ),
                    contentDescription = stringResource(
                        if (hasCertificate) R.string.connection_secure else R.string.connection_unsecure
                    ),
                    colorFilter = ColorFilter.tint(XS2ATheme.CURRENT.webViewIconColor)
                )
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                progress = loadingIndicatorProgress / 100f,
                color = XS2ATheme.CURRENT.tintColor
            )
        }

        // WebView
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                WebView(it).apply {
                    webView = this

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    addJavascriptInterface(XS2AJavascriptInterface(callbackHandler), "App")

                    webViewClient = object : WebViewClient() {
                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                            view.loadUrl(url)
                            return true
                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)

                            currentUrl = url
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)

                            hasCertificate = view?.certificate != null
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, progress: Int) {
                            loadingIndicatorProgress = progress
                        }
                    }
                }
            }
        )
    }
}
