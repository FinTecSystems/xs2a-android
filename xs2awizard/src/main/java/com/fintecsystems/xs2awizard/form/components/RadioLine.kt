package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.RadioLineData
import kotlinx.serialization.json.*

@Composable
fun LabelledRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    disabled: Boolean = false,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = XS2ATheme.CURRENT.tintColor),
            enabled = !disabled,
        )
        Text(
            text = label,
            color = XS2ATheme.CURRENT.textColor
        )

    }
}

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