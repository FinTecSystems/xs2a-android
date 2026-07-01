package com.fintecsystems.xs2awizard.screenshot.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.loadingIndicator.LoadingIndicator
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
class LoadingIndicatorScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = LoadingIndicator(modifier = Modifier.fillMaxWidth())

    @Test fun loadingIndicator_light() = composeRule.captureForTheme("loading_indicator_light", XS2ATheme.light) { Content() }
    @Test fun loadingIndicator_dark() = composeRule.captureForTheme("loading_indicator_dark", XS2ATheme.dark) { Content() }
}
