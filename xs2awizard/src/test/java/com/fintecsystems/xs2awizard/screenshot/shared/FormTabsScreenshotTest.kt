package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.components.shared.FormTabs
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
class FormTabsScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun formTabs() {
        composeRule.captureForThemes("form_tabs") {
            FormTabs(
                selected = 0,
                onSelectedChange = {},
                tabs = listOf("IBAN", "Account No.", "BIC"),
            )
        }
    }
}
