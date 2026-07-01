package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.FlickerLineData
import com.fintecsystems.xs2awizard.form.components.FlickerLine
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
class FlickerLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = FlickerLine(
        formData = FlickerLineData(
            name = "flicker",
            label = "ChipTAN",
            placeholder = "TAN",
            code = listOf(listOf(1, 0, 1, 0, 1), listOf(0, 1, 0, 1, 0), listOf(1, 0, 1, 0, 1)),
        ),
    )

    @Test fun flickerLine_light() = composeRule.captureForTheme("flicker_line_light", XS2ATheme.light) { Content() }
    @Test fun flickerLine_dark() = composeRule.captureForTheme("flicker_line_dark", XS2ATheme.dark) { Content() }
}
