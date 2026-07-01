package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
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
class FormTextFieldScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Empty() = FormTextField(value = "", onValueChange = {}, label = "IBAN", placeholder = "DE00 0000 0000 0000 0000 00")
    @Composable
    private fun WithValue() = FormTextField(value = "DE89 3704 0044 0532 0130 00", onValueChange = {}, label = "IBAN")
    @Composable
    private fun WithError() = FormTextField(value = "INVALID", onValueChange = {}, label = "IBAN", errorMessage = "Invalid IBAN format.")

    @Test fun formTextField_empty_light() = composeRule.captureForTheme("form_text_field_empty_light", XS2ATheme.light) { Empty() }
    @Test fun formTextField_empty_dark() = composeRule.captureForTheme("form_text_field_empty_dark", XS2ATheme.dark) { Empty() }
    @Test fun formTextField_withValue_light() = composeRule.captureForTheme("form_text_field_with_value_light", XS2ATheme.light) { WithValue() }
    @Test fun formTextField_withValue_dark() = composeRule.captureForTheme("form_text_field_with_value_dark", XS2ATheme.dark) { WithValue() }
    @Test fun formTextField_withError_light() = composeRule.captureForTheme("form_text_field_with_error_light", XS2ATheme.light) { WithError() }
    @Test fun formTextField_withError_dark() = composeRule.captureForTheme("form_text_field_with_error_dark", XS2ATheme.dark) { WithError() }
}
