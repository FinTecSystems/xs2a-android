package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine
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
class TextLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun textLine_empty() {
        composeRule.captureForThemes("text_line_empty") {
            TextLine(
                formData = TextLineData(
                    name = "iban",
                    label = "IBAN",
                    placeholder = "DE00 0000 0000 0000 0000 00",
                ),
                viewModel = createViewModel(),
            )
        }
    }

    @Test
    fun textLine_invalid() {
        composeRule.captureForThemes("text_line_invalid") {
            TextLine(
                formData = TextLineData(
                    name = "iban",
                    label = "IBAN",
                    placeholder = "DE00 0000 0000 0000 0000 00",
                    invalid = true,
                    validationError = "Invalid IBAN format.",
                ),
                viewModel = createViewModel(),
            )
        }
    }
}
