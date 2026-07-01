package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.components.ParagraphLine
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Test

class ParagraphLineScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    @Composable
    private fun Plain() = ParagraphLine(formData = ParagraphLineData(title = "Note", text = "This is a plain informational paragraph with no severity styling."), viewModel = createViewModel())
    @Composable
    private fun Info() = ParagraphLine(formData = ParagraphLineData(title = "Information", text = "Your session is active. Please complete the form.", severity = "info"), viewModel = createViewModel())
    @Composable
    private fun Warning() = ParagraphLine(formData = ParagraphLineData(title = "Warning", text = "Your session will expire in 5 minutes.", severity = "warning"), viewModel = createViewModel())
    @Composable
    private fun Error() = ParagraphLine(formData = ParagraphLineData(title = "Error", text = "Authentication failed. Please try again.", severity = "error"), viewModel = createViewModel())

    @Test fun paragraphLine_plain() = composeRule.captureForTheme("paragraph_line_plain", screenshotTheme) { Plain() }
    @Test fun paragraphLine_info() = composeRule.captureForTheme("paragraph_line_info", screenshotTheme) { Info() }
    @Test fun paragraphLine_warning() = composeRule.captureForTheme("paragraph_line_warning", screenshotTheme) { Warning() }
    @Test fun paragraphLine_error() = composeRule.captureForTheme("paragraph_line_error", screenshotTheme) { Error() }
}
