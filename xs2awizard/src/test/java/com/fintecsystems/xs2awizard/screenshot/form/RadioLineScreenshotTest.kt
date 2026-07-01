package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.RadioLineData
import com.fintecsystems.xs2awizard.form.components.RadioLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import kotlinx.serialization.json.JsonPrimitive

class RadioLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {

    override val baseName = "radio_line"

    @Composable
    override fun Content() = RadioLine(
        formData = RadioLineData(
            name = "account_type",
            label = "Select account type",
            value = JsonPrimitive(0),
            options = listOf(JsonPrimitive("Checking account"), JsonPrimitive("Savings account"), JsonPrimitive("Business account")),
        ),
    )

}
