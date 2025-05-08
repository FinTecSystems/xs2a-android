package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.SelectLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

/**
 * Shows an Select-InputField
 *
 * @param formData Data of this FormLine
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLine(formData: SelectLineData) {
    // We need an reactive value, because formData.value is not reactive.
    var textFieldValue by remember { mutableStateOf("") }

    fun setValue(index: Int) {
        when (formData.options) {
            is JsonArray -> {
                textFieldValue = formData.options.jsonArray[index].jsonPrimitive.content

                // The backend expects strings
                formData.value = JsonPrimitive(index.toString())
            }

            is JsonObject -> {
                textFieldValue = formData.options.values.elementAt(index).jsonPrimitive.content
                formData.value = JsonPrimitive(formData.options.keys.elementAt(index))
            }

            else -> throw IllegalArgumentException()
        }
    }

    if (formData.value?.jsonPrimitive?.content.isNullOrEmpty()) {
        setValue(0)
    } else {
        when (formData.options) {
            is JsonArray -> setValue(formData.value!!.jsonPrimitive.int)
            is JsonObject -> {
                val index = formData.options.keys.indexOf(formData.value!!.jsonPrimitive.content)
                setValue(index)
            }

            else -> throw IllegalArgumentException()
        }
    }

    var selectIsExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = selectIsExpanded,
        onExpandedChange = { selectIsExpanded = it },
    ) {
        FormTextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            value = textFieldValue,
            onValueChange = { },
            singleLine = true,
            // Replace tint = XS2ATheme.CURRENT.textColor.value with LocalContentColor
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = selectIsExpanded) },
            isError = formData.invalid,
            required = formData.required,
            errorMessage = formData.validationError,
            label = formData.label
        )

        ExposedDropdownMenu(
            expanded = selectIsExpanded,
            onDismissRequest = { selectIsExpanded = false },
            containerColor = XS2ATheme.CURRENT.surfaceColor.value
        ) {
            val optionsArray = when (formData.options) {
                is JsonArray -> formData.options.toList()
                is JsonObject -> formData.options.values.toList()
                else -> throw IllegalArgumentException()
            }

            optionsArray.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Column(
                            modifier = Modifier.padding(2.dp, 4.dp)
                        ) {
                            FormText(
                                text = option.jsonPrimitive.content,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                            )
                        }
                    },
                    onClick = {
                        setValue(index)
                        selectIsExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}