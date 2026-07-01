package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.RestartLineData
import com.fintecsystems.xs2awizard.form.components.RestartLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel

class RestartLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    override val baseName = "restart_line"

    @Composable
    override fun Content() = RestartLine(formData = RestartLineData(label = "Restart"), viewModel = createViewModel())

}
