package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.components.shared.FormTabs
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
class FormTabsScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = FormTabs(selected = 0, onSelectedChange = {}, tabs = listOf("IBAN", "Account No.", "BIC"))

    @Test fun formTabs_light() = composeRule.captureForTheme("form_tabs_light", XS2ATheme.light) { Content() }
    @Test fun formTabs_dark() = composeRule.captureForTheme("form_tabs_dark", XS2ATheme.dark) { Content() }
}
