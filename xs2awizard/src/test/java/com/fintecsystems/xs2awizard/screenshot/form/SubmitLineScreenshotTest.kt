package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.SubmitLineData
import com.fintecsystems.xs2awizard.form.components.SubmitLine
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Test

class SubmitLineScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {

    @Composable
    private fun NoBack() = SubmitLine(formData = SubmitLineData(label = "Submit"), viewModel = createViewModel())
    @Composable
    private fun WithBack() = SubmitLine(formData = SubmitLineData(label = "Submit", backLabel = "Back"), viewModel = createViewModel().apply { enableBackButton = true })

    @Test fun submitLine_noBack() = composeRule.captureForTheme("submit_line_no_back", screenshotTheme) { NoBack() }
    @Test fun submitLine_withBack() = composeRule.captureForTheme("submit_line_with_back", screenshotTheme) { WithBack() }
}
