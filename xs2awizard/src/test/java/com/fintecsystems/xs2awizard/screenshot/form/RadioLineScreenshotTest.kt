package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.RadioLineData
import com.fintecsystems.xs2awizard.form.components.RadioLine
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
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
class RadioLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Composable
    private fun Content() = RadioLine(
        formData = RadioLineData(
            name = "account_type",
            label = "Select account type",
            value = JsonPrimitive(0),
            options = listOf(JsonPrimitive("Checking account"), JsonPrimitive("Savings account"), JsonPrimitive("Business account")),
        ),
    )

    @Test fun radioLine_light() = composeRule.captureForTheme("radio_line_light", XS2ATheme.light) { Content() }
    @Test fun radioLine_dark() = composeRule.captureForTheme("radio_line_dark", XS2ATheme.dark) { Content() }
}
