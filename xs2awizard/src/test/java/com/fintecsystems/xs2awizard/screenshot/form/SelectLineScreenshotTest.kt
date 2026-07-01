package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.ui.test.junit4.createComposeRule
import com.fintecsystems.xs2awizard.form.SelectLineData
import com.fintecsystems.xs2awizard.form.components.SelectLine
import com.fintecsystems.xs2awizard.screenshot.captureForThemes
import kotlinx.serialization.json.JsonArray
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
class SelectLineScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    fun selectLine() {
        composeRule.captureForThemes("select_line") {
            SelectLine(
                formData = SelectLineData(
                    name = "country",
                    label = "Country",
                    value = JsonPrimitive(0),
                    options = JsonArray(
                        listOf(
                            JsonPrimitive("Germany"),
                            JsonPrimitive("Austria"),
                            JsonPrimitive("Switzerland"),
                        )
                    ),
                ),
            )
        }
    }
}
