package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.components.ParagraphLine
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
class ParagraphLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Plain() = ParagraphLine(formData = ParagraphLineData(title = "Note", text = "This is a plain informational paragraph with no severity styling."), viewModel = createViewModel())
    @Composable
    private fun Info() = ParagraphLine(formData = ParagraphLineData(title = "Information", text = "Your session is active. Please complete the form.", severity = "info"), viewModel = createViewModel())
    @Composable
    private fun Warning() = ParagraphLine(formData = ParagraphLineData(title = "Warning", text = "Your session will expire in 5 minutes.", severity = "warning"), viewModel = createViewModel())
    @Composable
    private fun Error() = ParagraphLine(formData = ParagraphLineData(title = "Error", text = "Authentication failed. Please try again.", severity = "error"), viewModel = createViewModel())

    @Test fun paragraphLine_plain_light() = composeRule.captureForTheme("paragraph_line_plain_light", XS2ATheme.light) { Plain() }
    @Test fun paragraphLine_plain_dark() = composeRule.captureForTheme("paragraph_line_plain_dark", XS2ATheme.dark) { Plain() }
    @Test fun paragraphLine_info_light() = composeRule.captureForTheme("paragraph_line_info_light", XS2ATheme.light) { Info() }
    @Test fun paragraphLine_info_dark() = composeRule.captureForTheme("paragraph_line_info_dark", XS2ATheme.dark) { Info() }
    @Test fun paragraphLine_warning_light() = composeRule.captureForTheme("paragraph_line_warning_light", XS2ATheme.light) { Warning() }
    @Test fun paragraphLine_warning_dark() = composeRule.captureForTheme("paragraph_line_warning_dark", XS2ATheme.dark) { Warning() }
    @Test fun paragraphLine_error_light() = composeRule.captureForTheme("paragraph_line_error_light", XS2ATheme.light) { Error() }
    @Test fun paragraphLine_error_dark() = composeRule.captureForTheme("paragraph_line_error_dark", XS2ATheme.dark) { Error() }
}
