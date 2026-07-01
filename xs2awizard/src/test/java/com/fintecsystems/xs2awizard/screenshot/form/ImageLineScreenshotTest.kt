package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.ImageLineData
import com.fintecsystems.xs2awizard.form.components.ImageLine
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ImageLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    // A minimal 1×1 black PNG encoded as Base64.
    private val testImageBase64 =
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAC0lEQVQI12NgAAIABQAABjkB6QAAAABJRU5ErkJggg=="

    @Test
    fun imageLine() {
        composeRule.captureForThemes("image_line") {
            ImageLine(
                formData = ImageLineData(
                    data = testImageBase64,
                    align = "center",
                    description = "Bank logo",
                ),
            )
        }
    }
}
