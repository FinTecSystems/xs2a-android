package com.fintecsystems.xs2awizard.screenshot.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.components.networking.ConnectivityStatusBanner
import com.fintecsystems.xs2awizard.components.networking.ConnectionState
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ConnectivityStatusBannerScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun connectivityStatusBanner_disconnected() {
        composeRule.captureForThemes("connectivity_status_banner_disconnected") {
            ConnectivityStatusBanner(connectionState = ConnectionState.DISCONNECTED)
        }
    }

    @Test
    fun connectivityStatusBanner_connected() {
        // Banner is invisible when connected; captures the (empty) composition.
        composeRule.captureForThemes("connectivity_status_banner_connected") {
            ConnectivityStatusBanner(connectionState = ConnectionState.CONNECTED)
        }
    }
}
