package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.CaptchaLineData
import com.fintecsystems.xs2awizard.form.components.CaptchaLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CaptchaLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    // A minimal 1×1 black PNG encoded as Base64.
    private val testImageBase64 =
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAC0lEQVQI12NgAAIABQAABjkB6QAAAABJRU5ErkJggg=="

    @Composable
    private fun Content() = CaptchaLine(
        formData = CaptchaLineData(
            name = "captcha",
            label = "Enter the code shown below",
            placeholder = "Code",
            data = testImageBase64,
        ),
    )

    @Test fun captchaLine_light() = composeRule.captureForTheme("captcha_line_light", XS2ATheme.light) { Content() }
    @Test fun captchaLine_dark() = composeRule.captureForTheme("captcha_line_dark", XS2ATheme.dark) { Content() }
}
