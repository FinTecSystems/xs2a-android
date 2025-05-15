package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.form.RadioLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * A single RadioButton with an Label.
 *
 * @param selected Selection state.
 * @param onClick OnClick callback.
 * @param label Label of the RadioButton
 * @param enabled Enabled state of the Button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.RadioButton
            ),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled,
        )
        FormText(
            text = label,
            color = if (enabled) MaterialTheme.colorScheme.onBackground
            else RadioButtonDefaults.colors().disabledUnselectedColor
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
            formData.value?.jsonPrimitive?.intOrNull ?: 0
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

    Column(
        modifier = Modifier
            .selectableGroup()
            .semantics {
                formData.label?.let {
                    contentDescription = it
                }
            },
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        formData.label?.let {
            FormText(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics {
                    invisibleToUser()
                }
            )
        }

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
                    enabled = radioElement.jsonObject["disabled"]?.jsonPrimitive?.booleanOrNull != true
                )
            }
        }
    }
}