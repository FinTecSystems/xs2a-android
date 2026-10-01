package com.fintecsystems.xs2awizard.screenshot.components

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.components.networking.ConnectivityStatusBanner
import com.fintecsystems.xs2awizard.components.networking.ConnectionState
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme

// Only the DISCONNECTED state is tested: it renders the visible banner.
// The CONNECTED state renders nothing (AnimatedVisibility visible=false on first composition
// produces a zero-size layout), which causes Roborazzi to throw when capturing an empty root.
class ConnectivityStatusBannerScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    override val baseName = "connectivity_status_banner"

    @Composable
    override fun Content() = ConnectivityStatusBanner(connectionState = ConnectionState.DISCONNECTED)
}
