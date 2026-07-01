package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.RestartLineData
import com.fintecsystems.xs2awizard.form.components.RestartLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.createViewModel

class RestartLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    override val baseName = "restart_line"

    @Composable
    override fun Content() = RestartLine(formData = RestartLineData(label = "Restart"), viewModel = createViewModel())

}
