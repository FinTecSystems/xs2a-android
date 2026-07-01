package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.DescriptionLineData
import com.fintecsystems.xs2awizard.form.components.DescriptionLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel

class DescriptionLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    override val baseName = "description_line"

    @Composable
    override fun Content() = DescriptionLine(
        formData = DescriptionLineData(text = "Please enter your bank credentials to proceed with the payment."),
        viewModel = createViewModel(),
    )

}
