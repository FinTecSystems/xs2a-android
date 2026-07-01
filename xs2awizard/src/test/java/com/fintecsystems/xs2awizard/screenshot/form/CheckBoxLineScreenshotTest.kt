package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.form.components.CheckBoxLine
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CheckBoxLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun checkBoxLine_unchecked() {
        composeRule.captureForThemes("check_box_line_unchecked") {
            CheckBoxLine(
                formData = CheckBoxLineData(
                    name = "consent",
                    label = "I agree to the **terms and conditions**.",
                    value = JsonPrimitive(false),
                ),
                viewModel = createViewModel(),
            )
        }
    }

    @Test
    fun checkBoxLine_checked() {
        composeRule.captureForThemes("check_box_line_checked") {
            CheckBoxLine(
                formData = CheckBoxLineData(
                    name = "consent",
                    label = "I agree to the **terms and conditions**.",
                    value = JsonPrimitive(true),
                ),
                viewModel = createViewModel(),
            )
        }
    }

    @Test
    fun checkBoxLine_invalid() {
        composeRule.captureForThemes("check_box_line_invalid") {
            CheckBoxLine(
                formData = CheckBoxLineData(
                    name = "consent",
                    label = "I agree to the **terms and conditions**.",
                    value = JsonPrimitive(false),
                    invalid = true,
                    required = true,
                    validationError = "This field is required.",
                ),
                viewModel = createViewModel(),
            )
        }
    }
}
