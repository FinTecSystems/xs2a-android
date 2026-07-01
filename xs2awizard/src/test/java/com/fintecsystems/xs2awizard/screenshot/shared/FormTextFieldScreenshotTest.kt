package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Test

class FormTextFieldScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    @Composable
    private fun Empty() = FormTextField(value = "", onValueChange = {}, label = "IBAN", placeholder = "DE00 0000 0000 0000 0000 00")
    @Composable
    private fun WithValue() = FormTextField(value = "DE89 3704 0044 0532 0130 00", onValueChange = {}, label = "IBAN")
    @Composable
    private fun WithError() = FormTextField(value = "INVALID", onValueChange = {}, label = "IBAN", errorMessage = "Invalid IBAN format.")

    @Test fun formTextField_empty() = composeRule.captureForTheme("form_text_field_empty", screenshotTheme) { Empty() }
    @Test fun formTextField_withValue() = composeRule.captureForTheme("form_text_field_with_value", screenshotTheme) { WithValue() }
    @Test fun formTextField_withError() = composeRule.captureForTheme("form_text_field_with_error", screenshotTheme) { WithError() }
}
