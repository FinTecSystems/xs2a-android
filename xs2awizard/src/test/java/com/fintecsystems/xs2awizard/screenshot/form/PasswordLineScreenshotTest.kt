package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.PasswordLineData
import com.fintecsystems.xs2awizard.form.components.PasswordLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class PasswordLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Empty() = PasswordLine(formData = PasswordLineData(name = "password", label = "Password", placeholder = "Enter your password"))
    @Composable
    private fun Invalid() = PasswordLine(formData = PasswordLineData(name = "password", label = "Password", placeholder = "Enter your password", invalid = true, validationError = "Password is required."))

    @Test fun passwordLine_empty_light() = composeRule.captureForTheme("password_line_empty_light", XS2ATheme.light) { Empty() }
    @Test fun passwordLine_empty_dark() = composeRule.captureForTheme("password_line_empty_dark", XS2ATheme.dark) { Empty() }
    @Test fun passwordLine_invalid_light() = composeRule.captureForTheme("password_line_invalid_light", XS2ATheme.light) { Invalid() }
    @Test fun passwordLine_invalid_dark() = composeRule.captureForTheme("password_line_invalid_dark", XS2ATheme.dark) { Invalid() }
}
