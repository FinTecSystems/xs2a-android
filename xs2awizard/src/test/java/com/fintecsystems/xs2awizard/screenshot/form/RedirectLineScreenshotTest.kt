package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.RedirectLineData
import com.fintecsystems.xs2awizard.form.components.RedirectLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.createViewModel

class RedirectLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    override val baseName = "redirect_line"

    @Composable
    override fun Content() = RedirectLine(
        formData = RedirectLineData(name = "redirect", label = "Authenticate with your bank", backLabel = "Back", url = "https://example.com/auth"),
        viewModel = createViewModel(),
    )

}
