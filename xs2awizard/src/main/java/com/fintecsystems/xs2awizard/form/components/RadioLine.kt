package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.NoRippleTheme
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.RadioLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import kotlinx.serialization.json.*

/**
 * A single RadioButton with an Label.
 *
 * @param selected Selection state.
 * @param onClick OnClick callback.
 * @param label Label of the RadioButton
 * @param disabled Disabled state of the Button
 */
@Composable
fun LabelledRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    disabled: Boolean = false,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = !disabled,
                indication = rememberRipple(),
                interactionSource = interactionSource
            ),
    ) {
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                interactionSource = interactionSource,
                colors = RadioButtonDefaults.colors(
                    selectedColor = XS2ATheme.CURRENT.tintColor,
                    unselectedColor = XS2ATheme.CURRENT.unselectedColor,
                    disabledColor = XS2ATheme.CURRENT.disabledColor,
                ),
                enabled = !disabled,
            )
        }
        FormText(
            text = label,
            color = if (disabled) XS2ATheme.CURRENT.disabledColor else XS2ATheme.CURRENT.textColor
        )
    }
}

/**
 * Renders a group of [RadioButton].
 *
 * @param formData Data of this FormLine
 */
@Composable
fun RadioLine(formData: RadioLineData) {
    var selectedValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.int ?: 0
        )
    }

    /**
     * Callback for when the selected RadioButton changed.
     *
     * @param newValue
     */
    fun onSelectedChange(newValue: Int) {
        selectedValue = newValue
        // Update formData.value as well
        formData.value = JsonPrimitive(newValue)
    }

    LabelledContainer(label = formData.label) {
        Column(
            modifier = Modifier.offset((-14).dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            formData.options.forEachIndexed { index, radioElement ->
                if (radioElement is JsonPrimitive) {
                    LabelledRadioButton(
                        selected = index == selectedValue,
                        onClick = { onSelectedChange(index) },
                        label = radioElement.content
                    )
                } else {
                    LabelledRadioButton(
                        selected = index == selectedValue,
                        onClick = { onSelectedChange(index) },
                        label = radioElement.jsonObject["label"]?.jsonPrimitive?.content!!,
                        disabled = radioElement.jsonObject["disabled"]?.jsonPrimitive?.booleanOrNull
                            ?: false
                    )
                }
            }
        }
    }
}