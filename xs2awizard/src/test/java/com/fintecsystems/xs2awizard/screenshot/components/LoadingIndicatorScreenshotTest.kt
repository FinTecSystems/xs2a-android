package com.fintecsystems.xs2awizard.screenshot.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.components.loadingIndicator.LoadingIndicator
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme

class LoadingIndicatorScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    override val baseName = "loading_indicator"

    @Composable
    override fun Content() = LoadingIndicator(modifier = Modifier.fillMaxWidth())

}
