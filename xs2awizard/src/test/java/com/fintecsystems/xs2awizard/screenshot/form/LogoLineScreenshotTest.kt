package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.LogoLineData
import com.fintecsystems.xs2awizard.form.components.LogoLine
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
class LogoLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun logoLine() {
        // LogoLine loads the logo from a URL via Coil; in Robolectric/offline the image area will
        // render as an empty placeholder — which is the intended regression baseline.
        composeRule.captureForThemes("logo_line") {
            LogoLine(viewModel = createViewModel())
        }
    }
}
