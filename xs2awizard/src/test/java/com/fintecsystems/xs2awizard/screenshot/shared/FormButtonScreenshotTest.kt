package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.components.theme.XS2AColors
import com.fintecsystems.xs2awizard.form.components.shared.FormButton
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
class FormButtonScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun formButton_submit() {
        composeRule.captureForThemes("form_button_submit") {
            FormButton(
                label = "Submit",
                buttonStyle = ButtonStyle(
                    backgroundColor = XS2AColors.primary,
                    textColor = XS2AColors.textColorLight,
                ),
                onClick = {},
            )
        }
    }

    @Test
    fun formButton_abort() {
        composeRule.captureForThemes("form_button_abort") {
            FormButton(
                label = "Abort",
                buttonStyle = ButtonStyle(
                    backgroundColor = XS2AColors.darkGrey,
                    textColor = XS2AColors.textColor,
                ),
                onClick = {},
            )
        }
    }
}
