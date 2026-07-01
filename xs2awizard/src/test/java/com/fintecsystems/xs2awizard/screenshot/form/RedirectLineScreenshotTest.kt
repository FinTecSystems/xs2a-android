package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.RedirectLineData
import com.fintecsystems.xs2awizard.form.components.RedirectLine
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
class RedirectLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = RedirectLine(
        formData = RedirectLineData(name = "redirect", label = "Authenticate with your bank", backLabel = "Back", url = "https://example.com/auth"),
        viewModel = createViewModel(),
    )

    @Test fun redirectLine_light() = composeRule.captureForTheme("redirect_line_light", XS2ATheme.light) { Content() }
    @Test fun redirectLine_dark() = composeRule.captureForTheme("redirect_line_dark", XS2ATheme.dark) { Content() }
}
