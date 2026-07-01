package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.components.shared.FormText
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
class FormTextScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun formText_plain() {
        composeRule.captureForThemes("form_text_plain") {
            FormText(text = "Please enter your credentials to proceed.")
        }
    }

    @Test
    fun formText_withMarkup() {
        composeRule.captureForThemes("form_text_with_markup") {
            FormText(text = "Your **IBAN** is used to identify your bank account.")
        }
    }
}
