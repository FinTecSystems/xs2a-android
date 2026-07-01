package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.SubmitLineData
import com.fintecsystems.xs2awizard.form.components.SubmitLine
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
class SubmitLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun submitLine_withoutBackButton() {
        composeRule.captureForThemes("submit_line_no_back") {
            SubmitLine(
                formData = SubmitLineData(label = "Submit"),
                viewModel = createViewModel(),
            )
        }
    }

    @Test
    fun submitLine_withBackButton() {
        val viewModel = createViewModel().apply { enableBackButton = true }
        composeRule.captureForThemes("submit_line_with_back") {
            SubmitLine(
                formData = SubmitLineData(label = "Submit", backLabel = "Back"),
                viewModel = viewModel,
            )
        }
    }
}
