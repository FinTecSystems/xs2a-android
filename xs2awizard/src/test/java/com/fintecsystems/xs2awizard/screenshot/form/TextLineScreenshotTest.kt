package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine
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
class TextLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Empty() = TextLine(formData = TextLineData(name = "iban", label = "IBAN", placeholder = "DE00 0000 0000 0000 0000 00"), viewModel = createViewModel())
    @Composable
    private fun Invalid() = TextLine(formData = TextLineData(name = "iban", label = "IBAN", placeholder = "DE00 0000 0000 0000 0000 00", invalid = true, validationError = "Invalid IBAN format."), viewModel = createViewModel())

    @Test fun textLine_empty_light() = composeRule.captureForTheme("text_line_empty_light", XS2ATheme.light) { Empty() }
    @Test fun textLine_empty_dark() = composeRule.captureForTheme("text_line_empty_dark", XS2ATheme.dark) { Empty() }
    @Test fun textLine_invalid_light() = composeRule.captureForTheme("text_line_invalid_light", XS2ATheme.light) { Invalid() }
    @Test fun textLine_invalid_dark() = composeRule.captureForTheme("text_line_invalid_dark", XS2ATheme.dark) { Invalid() }
}
