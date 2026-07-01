package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.form.components.CheckBoxLine
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Test

class CheckBoxLineScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {

    @Composable
    private fun Unchecked() = CheckBoxLine(
        formData = CheckBoxLineData(name = "consent", label = "I agree to the **terms and conditions**.", value = JsonPrimitive(false)),
        viewModel = createViewModel(),
    )
    @Composable
    private fun Checked() = CheckBoxLine(
        formData = CheckBoxLineData(name = "consent", label = "I agree to the **terms and conditions**.", value = JsonPrimitive(true)),
        viewModel = createViewModel(),
    )
    @Composable
    private fun Invalid() = CheckBoxLine(
        formData = CheckBoxLineData(name = "consent", label = "I agree to the **terms and conditions**.", value = JsonPrimitive(false), invalid = true, required = true, validationError = "This field is required."),
        viewModel = createViewModel(),
    )

    @Test fun checkBoxLine_unchecked() = composeRule.captureForTheme("check_box_line_unchecked", screenshotTheme) { Unchecked() }
    @Test fun checkBoxLine_checked() = composeRule.captureForTheme("check_box_line_checked", screenshotTheme) { Checked() }
    @Test fun checkBoxLine_invalid() = composeRule.captureForTheme("check_box_line_invalid", screenshotTheme) { Invalid() }
}
