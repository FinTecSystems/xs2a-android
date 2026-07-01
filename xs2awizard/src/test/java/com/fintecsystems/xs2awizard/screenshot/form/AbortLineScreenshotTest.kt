package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.AbortLineData
import com.fintecsystems.xs2awizard.form.components.AbortLine
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AbortLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun abortLine() {
        composeRule.captureForThemes("abort_line") {
            AbortLine(
                formData = AbortLineData(label = "Abort"),
                viewModel = createViewModel(),
            )
        }
    }
}
