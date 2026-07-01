package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.DescriptionLineData
import com.fintecsystems.xs2awizard.form.components.DescriptionLine
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
class DescriptionLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun descriptionLine() {
        composeRule.captureForThemes("description_line") {
            DescriptionLine(
                formData = DescriptionLineData(
                    text = "Please enter your bank credentials to proceed with the payment.",
                ),
                viewModel = createViewModel(),
            )
        }
    }
}
