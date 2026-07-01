package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.FlickerLineData
import com.fintecsystems.xs2awizard.form.components.FlickerLine
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
class FlickerLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun flickerLine() {
        // Captures the initial (non-animated) frame of the ChipTAN flicker code renderer.
        composeRule.captureForThemes("flicker_line") {
            FlickerLine(
                formData = FlickerLineData(
                    name = "flicker",
                    label = "ChipTAN",
                    placeholder = "TAN",
                    // Sample flicker code: 5 columns, alternating black/white bars
                    code = listOf(
                        listOf(1, 0, 1, 0, 1),
                        listOf(0, 1, 0, 1, 0),
                        listOf(1, 0, 1, 0, 1),
                    ),
                ),
            )
        }
    }
}
