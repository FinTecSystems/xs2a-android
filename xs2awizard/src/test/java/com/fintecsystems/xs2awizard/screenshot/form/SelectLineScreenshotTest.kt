package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.SelectLineData
import com.fintecsystems.xs2awizard.form.components.SelectLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SelectLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = SelectLine(
        formData = SelectLineData(
            name = "country",
            label = "Country",
            value = JsonPrimitive(0),
            options = JsonArray(listOf(JsonPrimitive("Germany"), JsonPrimitive("Austria"), JsonPrimitive("Switzerland"))),
        ),
    )

    @Test fun selectLine_light() = composeRule.captureForTheme("select_line_light", XS2ATheme.light) { Content() }
    @Test fun selectLine_dark() = composeRule.captureForTheme("select_line_dark", XS2ATheme.dark) { Content() }
}
