package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.FlickerLineData
import com.fintecsystems.xs2awizard.form.components.FlickerLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters

class FlickerLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    override val baseName = "flicker_line"

    @Composable
    override fun Content() = FlickerLine(
        formData = FlickerLineData(
            name = "flicker",
            label = "ChipTAN",
            placeholder = "TAN",
            code = listOf(listOf(1, 0, 1, 0, 1), listOf(0, 1, 0, 1, 0), listOf(1, 0, 1, 0, 1)),
        ),
    )

}
