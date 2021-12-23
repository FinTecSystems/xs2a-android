package com.fintecsystems.xs2awizard.form.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.snap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.theme.XS2AColors
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.FlickerLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlin.math.max
import kotlin.math.min

/**
 * Displays an Flicker-Element with a [FormTextField] below it.
 */
@Composable
fun FlickerLine(formData: FlickerLineData) {
    val colors = listOf(
        XS2AColors.flickerBackground,
        XS2AColors.flickerForeground
    )

    val barColors = mutableListOf(
        remember { Animatable(Color.Black) },
        remember { Animatable(Color.Black) },
        remember { Animatable(Color.Black) },
        remember { Animatable(Color.Black) },
        remember { Animatable(Color.Black) },
    )

    var flickerContainerWidth by remember { mutableStateOf(0) }
    val flickerContainerMinWidth = 100
    val sizeStepSize = 10

    var fps by remember { mutableStateOf(10) }
    val fpsStepSize = 2

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                formData.value?.jsonPrimitive?.content ?: ""
            )
        )
    }

    LaunchedEffect(null) {
        var currentStep = 0

        while (true) {
            formData.code[currentStep].forEachIndexed { index, i ->
                barColors[index].animateTo(
                    colors[i],
                    snap(0)
                )
            }

            currentStep = (currentStep + 1) % formData.code.size

            delay(1000L / fps)
        }
    }

    fun onValueChange(newValue: TextFieldValue) {
        textFieldValue = newValue
        formData.value = JsonPrimitive(newValue.text)
    }

    LabelledContainer(formData.label) {
        // Flicker container
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Flicker controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        if (flickerContainerWidth <= 0)
                            flickerContainerWidth = it.size.width
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Size
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = XS2ATheme.CURRENT.tintColor),
                        onClick = {
                            flickerContainerWidth =
                                max(flickerContainerMinWidth, flickerContainerWidth - sizeStepSize)
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_flicker_smaller),
                            contentDescription = stringResource(id = R.string.flicker_smaller),
                            colorFilter = ColorFilter.tint(XS2ATheme.CURRENT.onTintColor)
                        )
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = XS2ATheme.CURRENT.tintColor),
                        onClick = { flickerContainerWidth += sizeStepSize }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_flicker_bigger),
                            contentDescription = stringResource(id = R.string.flicker_bigger),
                            colorFilter = ColorFilter.tint(XS2ATheme.CURRENT.onTintColor)
                        )
                    }
                }

                // Speed
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = XS2ATheme.CURRENT.tintColor),
                        onClick = { fps = max(fps - fpsStepSize, 1) }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_flicker_slower),
                            contentDescription = stringResource(id = R.string.flicker_slower),
                            colorFilter = ColorFilter.tint(XS2ATheme.CURRENT.onTintColor)
                        )
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = XS2ATheme.CURRENT.tintColor),
                        onClick = { fps = min(fps + fpsStepSize, 60) }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_flicker_faster),
                            contentDescription = stringResource(id = R.string.flicker_faster),
                            colorFilter = ColorFilter.tint(XS2ATheme.CURRENT.onTintColor)
                        )
                    }
                }
            }

            // Flicker Bars
            Row(
                Modifier
                    .background(Color.Black)
                    .padding(7.dp, 3.5.dp)
                    .height(100.dp)
                    .width(with(LocalDensity.current) { flickerContainerWidth.toDp() }),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                barColors.forEachIndexed { index, it ->
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .weight(1f)
                            .background(color = it.value)
                    ) {
                        if (index == 0 || index == barColors.lastIndex) {
                            Image(
                                painter = painterResource(R.drawable.ic_flicker_marker),
                                contentDescription = stringResource(id = R.string.flicker_marker),
                                colorFilter = ColorFilter.tint(XS2AColors.flickerMarker),
                                alignment = Alignment.TopCenter,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        FormTextField(
            value = textFieldValue,
            onValueChange = ::onValueChange,
            placeholder = formData.placeholder
        )
    }
}
