package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Test

class TextLineScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {

    @Composable
    private fun Empty() = TextLine(formData = TextLineData(name = "iban", label = "IBAN", placeholder = "DE00 0000 0000 0000 0000 00"), viewModel = createViewModel())
    @Composable
    private fun Invalid() = TextLine(formData = TextLineData(name = "iban", label = "IBAN", placeholder = "DE00 0000 0000 0000 0000 00", invalid = true, validationError = "Invalid IBAN format."), viewModel = createViewModel())

    @Test fun textLine_empty() = composeRule.captureForTheme("text_line_empty", screenshotTheme) { Empty() }
    @Test fun textLine_invalid() = composeRule.captureForTheme("text_line_invalid", screenshotTheme) { Invalid() }
}
