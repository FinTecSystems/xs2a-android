package com.fintecsystems.xs2awizard.screenshot.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.components.loadingIndicator.LoadingIndicator
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
class LoadingIndicatorScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun loadingIndicator() {
        composeRule.captureForThemes("loading_indicator") {
            LoadingIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}
