package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.AutoSubmitLineData
import com.fintecsystems.xs2awizard.form.components.AutoSubmitLine
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
class AutoSubmitLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun autoSubmitLine() {
        // AutoSubmitLine shows a loading indicator while waiting for auto-submit.
        composeRule.captureForThemes("auto_submit_line") {
            AutoSubmitLine(
                formData = AutoSubmitLineData(interval = 30_000),
                viewModel = createViewModel(),
            )
        }
    }
}
