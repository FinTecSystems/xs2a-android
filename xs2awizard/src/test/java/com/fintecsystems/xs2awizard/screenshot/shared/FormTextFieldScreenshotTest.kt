package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
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
class FormTextFieldScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun formTextField_empty() {
        composeRule.captureForThemes("form_text_field_empty") {
            FormTextField(
                value = "",
                onValueChange = {},
                label = "IBAN",
                placeholder = "DE00 0000 0000 0000 0000 00",
            )
        }
    }

    @Test
    fun formTextField_withValue() {
        composeRule.captureForThemes("form_text_field_with_value") {
            FormTextField(
                value = "DE89 3704 0044 0532 0130 00",
                onValueChange = {},
                label = "IBAN",
            )
        }
    }

    @Test
    fun formTextField_withError() {
        composeRule.captureForThemes("form_text_field_with_error") {
            FormTextField(
                value = "INVALID",
                onValueChange = {},
                label = "IBAN",
                errorMessage = "Invalid IBAN format.",
            )
        }
    }
}
