package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.RedirectLineData
import com.fintecsystems.xs2awizard.form.components.RedirectLine
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
class RedirectLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun redirectLine() {
        composeRule.captureForThemes("redirect_line") {
            RedirectLine(
                formData = RedirectLineData(
                    name = "redirect",
                    label = "Authenticate with your bank",
                    backLabel = "Back",
                    url = "https://example.com/auth",
                ),
                viewModel = createViewModel(),
            )
        }
    }
}
