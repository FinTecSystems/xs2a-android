package com.fintecsystems.xs2awizard.screenshot.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.networking.ConnectivityStatusBanner
import com.fintecsystems.xs2awizard.components.networking.ConnectionState
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ConnectivityStatusBannerScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Disconnected() = ConnectivityStatusBanner(connectionState = ConnectionState.DISCONNECTED)
    @Composable
    private fun Connected() = ConnectivityStatusBanner(connectionState = ConnectionState.CONNECTED)

    @Test fun connectivityBanner_disconnected_light() = composeRule.captureForTheme("connectivity_status_banner_disconnected_light", XS2ATheme.light) { Disconnected() }
    @Test fun connectivityBanner_disconnected_dark() = composeRule.captureForTheme("connectivity_status_banner_disconnected_dark", XS2ATheme.dark) { Disconnected() }
    @Test fun connectivityBanner_connected_light() = composeRule.captureForTheme("connectivity_status_banner_connected_light", XS2ATheme.light) { Connected() }
    @Test fun connectivityBanner_connected_dark() = composeRule.captureForTheme("connectivity_status_banner_connected_dark", XS2ATheme.dark) { Connected() }
}
