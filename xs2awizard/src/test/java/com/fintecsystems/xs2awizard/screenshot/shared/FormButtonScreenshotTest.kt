package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2AColors
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.form.components.shared.FormButton
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
class FormButtonScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Submit() = FormButton(label = "Submit", buttonStyle = ButtonStyle(backgroundColor = XS2AColors.primary, textColor = XS2AColors.textColorLight), onClick = {})
    @Composable
    private fun Abort() = FormButton(label = "Abort", buttonStyle = ButtonStyle(backgroundColor = XS2AColors.darkGrey, textColor = XS2AColors.textColor), onClick = {})

    @Test fun formButton_submit_light() = composeRule.captureForTheme("form_button_submit_light", XS2ATheme.light) { Submit() }
    @Test fun formButton_submit_dark() = composeRule.captureForTheme("form_button_submit_dark", XS2ATheme.dark) { Submit() }
    @Test fun formButton_abort_light() = composeRule.captureForTheme("form_button_abort_light", XS2ATheme.light) { Abort() }
    @Test fun formButton_abort_dark() = composeRule.captureForTheme("form_button_abort_dark", XS2ATheme.dark) { Abort() }
}
