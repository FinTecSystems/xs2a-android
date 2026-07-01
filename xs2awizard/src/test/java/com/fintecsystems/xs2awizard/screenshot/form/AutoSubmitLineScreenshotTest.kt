package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.AutoSubmitLineData
import com.fintecsystems.xs2awizard.form.components.AutoSubmitLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AutoSubmitLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = AutoSubmitLine(
        formData = AutoSubmitLineData(interval = 30_000),
        viewModel = createViewModel(),
    )

    @Test fun autoSubmitLine_light() = composeRule.captureForTheme("auto_submit_line_light", XS2ATheme.light) { Content() }
    @Test fun autoSubmitLine_dark() = composeRule.captureForTheme("auto_submit_line_dark", XS2ATheme.dark) { Content() }
}
