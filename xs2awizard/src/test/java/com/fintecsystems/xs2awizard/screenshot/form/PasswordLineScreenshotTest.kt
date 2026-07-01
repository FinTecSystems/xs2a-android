package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.PasswordLineData
import com.fintecsystems.xs2awizard.form.components.PasswordLine
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Test

class PasswordLineScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    @Composable
    private fun Empty() = PasswordLine(formData = PasswordLineData(name = "password", label = "Password", placeholder = "Enter your password"))
    @Composable
    private fun Invalid() = PasswordLine(formData = PasswordLineData(name = "password", label = "Password", placeholder = "Enter your password", invalid = true, validationError = "Password is required."))

    @Test fun passwordLine_empty() = composeRule.captureForTheme("password_line_empty", screenshotTheme) { Empty() }
    @Test fun passwordLine_invalid() = composeRule.captureForTheme("password_line_invalid", screenshotTheme) { Invalid() }
}
