package com.fintecsystems.xs2awizard.screenshot.shared

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.components.shared.FormTabs
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme

class FormTabsScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    override val baseName = "form_tabs"

    @Composable
    override fun Content() = FormTabs(selected = 0, onSelectedChange = {}, tabs = listOf("IBAN", "Account No.", "BIC"))

}
