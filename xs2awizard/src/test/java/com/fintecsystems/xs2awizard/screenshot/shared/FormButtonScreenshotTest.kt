package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.components.theme.XS2AColors
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.form.components.shared.FormButton
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Test

class FormButtonScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    @Composable
    private fun Submit() = FormButton(label = "Submit", buttonStyle = ButtonStyle(backgroundColor = XS2AColors.primary, textColor = XS2AColors.textColorLight), onClick = {})
    @Composable
    private fun Abort() = FormButton(label = "Abort", buttonStyle = ButtonStyle(backgroundColor = XS2AColors.darkGrey, textColor = XS2AColors.textColor), onClick = {})

    @Test fun formButton_submit() = composeRule.captureForTheme("form_button_submit", screenshotTheme) { Submit() }
    @Test fun formButton_abort() = composeRule.captureForTheme("form_button_abort", screenshotTheme) { Abort() }
}
