package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Test

class FormTextScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {

    @Composable
    private fun Plain() = FormText(text = "Please enter your credentials to proceed.")
    @Composable
    private fun WithMarkup() = FormText(text = "Your **IBAN** is used to identify your bank account.")

    @Test fun formText_plain() = composeRule.captureForTheme("form_text_plain", screenshotTheme) { Plain() }
    @Test fun formText_withMarkup() = composeRule.captureForTheme("form_text_with_markup", screenshotTheme) { WithMarkup() }
}
