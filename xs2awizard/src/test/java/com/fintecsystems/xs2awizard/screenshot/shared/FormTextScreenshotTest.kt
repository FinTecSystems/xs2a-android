package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.components.shared.FormText
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
class FormTextScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Plain() = FormText(text = "Please enter your credentials to proceed.")
    @Composable
    private fun WithMarkup() = FormText(text = "Your **IBAN** is used to identify your bank account.")

    @Test fun formText_plain_light() = composeRule.captureForTheme("form_text_plain_light", XS2ATheme.light) { Plain() }
    @Test fun formText_plain_dark() = composeRule.captureForTheme("form_text_plain_dark", XS2ATheme.dark) { Plain() }
    @Test fun formText_withMarkup_light() = composeRule.captureForTheme("form_text_with_markup_light", XS2ATheme.light) { WithMarkup() }
    @Test fun formText_withMarkup_dark() = composeRule.captureForTheme("form_text_with_markup_dark", XS2ATheme.dark) { WithMarkup() }
}
