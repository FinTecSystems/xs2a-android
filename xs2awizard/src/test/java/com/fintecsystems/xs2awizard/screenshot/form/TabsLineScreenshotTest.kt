package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.TabsLineData
import com.fintecsystems.xs2awizard.form.components.TabsLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class TabsLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = TabsLine(
        formData = TabsLineData(action = "switch_tab", selected = "iban", tabs = linkedMapOf("iban" to "IBAN", "account_number" to "Account No.")),
        viewModel = createViewModel(),
    )

    @Test fun tabsLine_light() = composeRule.captureForTheme("tabs_line_light", XS2ATheme.light) { Content() }
    @Test fun tabsLine_dark() = composeRule.captureForTheme("tabs_line_dark", XS2ATheme.dark) { Content() }
}
