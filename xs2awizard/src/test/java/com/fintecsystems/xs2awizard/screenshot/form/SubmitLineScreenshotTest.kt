package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.SubmitLineData
import com.fintecsystems.xs2awizard.form.components.SubmitLine
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
class SubmitLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun NoBack() = SubmitLine(formData = SubmitLineData(label = "Submit"), viewModel = createViewModel())
    @Composable
    private fun WithBack() = SubmitLine(formData = SubmitLineData(label = "Submit", backLabel = "Back"), viewModel = createViewModel().apply { enableBackButton = true })

    @Test fun submitLine_noBack_light() = composeRule.captureForTheme("submit_line_no_back_light", XS2ATheme.light) { NoBack() }
    @Test fun submitLine_noBack_dark() = composeRule.captureForTheme("submit_line_no_back_dark", XS2ATheme.dark) { NoBack() }
    @Test fun submitLine_withBack_light() = composeRule.captureForTheme("submit_line_with_back_light", XS2ATheme.light) { WithBack() }
    @Test fun submitLine_withBack_dark() = composeRule.captureForTheme("submit_line_with_back_dark", XS2ATheme.dark) { WithBack() }
}
