package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.PasswordLineData
import com.fintecsystems.xs2awizard.form.components.PasswordLine
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class PasswordLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun passwordLine_empty() {
        composeRule.captureForThemes("password_line_empty") {
            PasswordLine(
                formData = PasswordLineData(
                    name = "password",
                    label = "Password",
                    placeholder = "Enter your password",
                ),
            )
        }
    }

    @Test
    fun passwordLine_invalid() {
        composeRule.captureForThemes("password_line_invalid") {
            PasswordLine(
                formData = PasswordLineData(
                    name = "password",
                    label = "Password",
                    placeholder = "Enter your password",
                    invalid = true,
                    validationError = "Password is required.",
                ),
            )
        }
    }
}
