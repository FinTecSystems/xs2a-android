package com.fintecsystems.xs2awizard.screenshot.components

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.components.networking.ConnectivityStatusBanner
import com.fintecsystems.xs2awizard.components.networking.ConnectionState
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Test

class ConnectivityStatusBannerScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    @Composable
    private fun Disconnected() = ConnectivityStatusBanner(connectionState = ConnectionState.DISCONNECTED)
    @Composable
    private fun Connected() = ConnectivityStatusBanner(connectionState = ConnectionState.CONNECTED)

    @Test fun connectivityBanner_disconnected() = composeRule.captureForTheme("connectivity_status_banner_disconnected", screenshotTheme) { Disconnected() }
    @Test fun connectivityBanner_connected() = composeRule.captureForTheme("connectivity_status_banner_connected", screenshotTheme) { Connected() }
}
