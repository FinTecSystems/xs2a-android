package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.TabsLineData
import com.fintecsystems.xs2awizard.form.components.TabsLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import com.fintecsystems.xs2awizard.screenshot.createViewModel

class TabsLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    override val baseName = "tabs_line"

    @Composable
    override fun Content() = TabsLine(
        formData = TabsLineData(action = "switch_tab", selected = "iban", tabs = linkedMapOf("iban" to "IBAN", "account_number" to "Account No.")),
        viewModel = createViewModel(),
    )

}
