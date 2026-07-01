package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.RadioLineData
import com.fintecsystems.xs2awizard.form.components.RadioLine
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class RadioLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun radioLine() {
        composeRule.captureForThemes("radio_line") {
            RadioLine(
                formData = RadioLineData(
                    name = "account_type",
                    label = "Select account type",
                    value = JsonPrimitive(0),
                    options = listOf(
                        JsonPrimitive("Checking account"),
                        JsonPrimitive("Savings account"),
                        JsonPrimitive("Business account"),
                    ),
                ),
            )
        }
    }
}
