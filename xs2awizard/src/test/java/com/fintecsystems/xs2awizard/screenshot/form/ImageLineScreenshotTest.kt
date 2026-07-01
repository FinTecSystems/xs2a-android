package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.ImageLineData
import com.fintecsystems.xs2awizard.form.components.ImageLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme

class ImageLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    // A minimal 1×1 black PNG encoded as Base64.
    private val testImageBase64 =
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAC0lEQVQI12NgAAIABQAABjkB6QAAAABJRU5ErkJggg=="

    override val baseName = "image_line"

    @Composable
    override fun Content() = ImageLine(
        formData = ImageLineData(data = testImageBase64, align = "center", description = "Bank logo"),
    )

}
