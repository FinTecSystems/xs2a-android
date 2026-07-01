package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.AutoSubmitLineData
import com.fintecsystems.xs2awizard.form.components.AutoSubmitLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel

class AutoSubmitLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    override val baseName = "auto_submit_line"

    @Composable
    override fun Content() = AutoSubmitLine(
        formData = AutoSubmitLineData(interval = 30_000),
        viewModel = createViewModel(),
    )

}
