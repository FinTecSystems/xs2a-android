package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.form.SelectLineData
import com.fintecsystems.xs2awizard.form.components.SelectLine
import com.fintecsystems.xs2awizard.screenshot.SingleContentScreenshotTest
import com.fintecsystems.xs2awizard.screenshot.SCREENSHOT_THEMES
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive

class SelectLineScreenshotTest(screenshotTheme: ScreenshotTheme) : SingleContentScreenshotTest(screenshotTheme) {
    companion object {
        @JvmStatic @Parameters(name = "{0}") fun themes() = SCREENSHOT_THEMES
    }


    override val baseName = "select_line"

    @Composable
    override fun Content() = SelectLine(
        formData = SelectLineData(
            name = "country",
            label = "Country",
            value = JsonPrimitive(0),
            options = JsonArray(listOf(JsonPrimitive("Germany"), JsonPrimitive("Austria"), JsonPrimitive("Switzerland"))),
        ),
    )

}
