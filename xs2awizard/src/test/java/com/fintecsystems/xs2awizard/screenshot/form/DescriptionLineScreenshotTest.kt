package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.DescriptionLineData
import com.fintecsystems.xs2awizard.form.components.DescriptionLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class DescriptionLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = DescriptionLine(
        formData = DescriptionLineData(text = "Please enter your bank credentials to proceed with the payment."),
        viewModel = createViewModel(),
    )

    @Test fun descriptionLine_light() = composeRule.captureForTheme("description_line_light", XS2ATheme.light) { Content() }
    @Test fun descriptionLine_dark() = composeRule.captureForTheme("description_line_dark", XS2ATheme.dark) { Content() }
}
