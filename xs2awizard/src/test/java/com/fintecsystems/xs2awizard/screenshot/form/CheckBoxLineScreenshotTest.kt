package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.form.components.CheckBoxLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CheckBoxLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Unchecked() = CheckBoxLine(
        formData = CheckBoxLineData(name = "consent", label = "I agree to the **terms and conditions**.", value = JsonPrimitive(false)),
        viewModel = createViewModel(),
    )
    @Composable
    private fun Checked() = CheckBoxLine(
        formData = CheckBoxLineData(name = "consent", label = "I agree to the **terms and conditions**.", value = JsonPrimitive(true)),
        viewModel = createViewModel(),
    )
    @Composable
    private fun Invalid() = CheckBoxLine(
        formData = CheckBoxLineData(name = "consent", label = "I agree to the **terms and conditions**.", value = JsonPrimitive(false), invalid = true, required = true, validationError = "This field is required."),
        viewModel = createViewModel(),
    )

    @Test fun checkBoxLine_unchecked_light() = composeRule.captureForTheme("check_box_line_unchecked_light", XS2ATheme.light) { Unchecked() }
    @Test fun checkBoxLine_unchecked_dark() = composeRule.captureForTheme("check_box_line_unchecked_dark", XS2ATheme.dark) { Unchecked() }
    @Test fun checkBoxLine_checked_light() = composeRule.captureForTheme("check_box_line_checked_light", XS2ATheme.light) { Checked() }
    @Test fun checkBoxLine_checked_dark() = composeRule.captureForTheme("check_box_line_checked_dark", XS2ATheme.dark) { Checked() }
    @Test fun checkBoxLine_invalid_light() = composeRule.captureForTheme("check_box_line_invalid_light", XS2ATheme.light) { Invalid() }
    @Test fun checkBoxLine_invalid_dark() = composeRule.captureForTheme("check_box_line_invalid_dark", XS2ATheme.dark) { Invalid() }
}
