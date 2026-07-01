package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.TabsLineData
import com.fintecsystems.xs2awizard.form.components.TabsLine
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
class TabsLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun tabsLine() {
        composeRule.captureForThemes("tabs_line") {
            TabsLine(
                formData = TabsLineData(
                    action = "switch_tab",
                    selected = "iban",
                    tabs = linkedMapOf(
                        "iban" to "IBAN",
                        "account_number" to "Account No.",
                    ),
                ),
                viewModel = createViewModel(),
            )
        }
    }
}
