package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.components.ParagraphLine
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ParagraphLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun paragraphLine_plain() {
        composeRule.captureForThemes("paragraph_line_plain") {
            ParagraphLine(
                formData = ParagraphLineData(
                    title = "Note",
                    text = "This is a plain informational paragraph with no severity styling.",
                ),
                viewModel = createViewModel(),
            )
        }
    }

    @Test
    fun paragraphLine_info() {
        composeRule.captureForThemes("paragraph_line_info") {
            ParagraphLine(
                formData = ParagraphLineData(
                    title = "Information",
                    text = "Your session is active. Please complete the form.",
                    severity = "info",
                ),
                viewModel = createViewModel(),
            )
        }
    }

    @Test
    fun paragraphLine_warning() {
        composeRule.captureForThemes("paragraph_line_warning") {
            ParagraphLine(
                formData = ParagraphLineData(
                    title = "Warning",
                    text = "Your session will expire in 5 minutes.",
                    severity = "warning",
                ),
                viewModel = createViewModel(),
            )
        }
    }

    @Test
    fun paragraphLine_error() {
        composeRule.captureForThemes("paragraph_line_error") {
            ParagraphLine(
                formData = ParagraphLineData(
                    title = "Error",
                    text = "Authentication failed. Please try again.",
                    severity = "error",
                ),
                viewModel = createViewModel(),
            )
        }
    }
}
