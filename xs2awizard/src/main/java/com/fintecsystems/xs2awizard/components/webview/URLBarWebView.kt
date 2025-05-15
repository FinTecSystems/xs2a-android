package com.fintecsystems.xs2awizard.components.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
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
                viewModel.redirectionCallback(success)
            }
        }
    }

    DisposableEffect(targetUrl, webView) {
        currentUrl = targetUrl
        webView?.loadUrl(targetUrl ?: "about:blank")

        onDispose { /* no-op */ }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        // Top Bar
        Column(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .zIndex(1f)
                .background(MaterialTheme.colorScheme.surface)
                .semantics {
                    isTraversalGroup = true
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    modifier = Modifier.width(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                    onClick = { viewModel.closeWebView() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.close_webview),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }

                Text(
                    text = currentUrl?.toUri()?.host ?: currentUrl ?: "",
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
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
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }

            if (loadingIndicatorProgress != 100) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .semantics {
                            liveRegion = LiveRegionMode.Polite
                        },
                    progress = { loadingIndicatorProgress / 100f },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // WebView
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .focusable()
                .semantics {
                    traversalIndex = -1f
                },
            factory = {
                WebView(it).apply {
                    webView = this

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                    settings.domStorageEnabled = true
                    settings.javaScriptEnabled = true
                    addJavascriptInterface(XS2AJavascriptInterface(callbackHandler), "App")

                    webViewClient = object : WebViewClient() {
                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                            if (viewModel.isRedirectDeepLink(url)) {
                                viewModel.redirectionCallback(true)
                                return true
                            }

                            currentUrl = url
                            view.loadUrl(url)
                            return true
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
