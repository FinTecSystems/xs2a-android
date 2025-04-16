package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
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
@Composable
fun SelectLine(formData: SelectLineData) {
    // We need an reactive value, because formData.value is not reactive.
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    fun setValue(index: Int) {
        when (formData.options) {
            is JsonArray -> {
                textFieldValue =
                    TextFieldValue(formData.options.jsonArray[index].jsonPrimitive.content)

                // The backend expects strings
                formData.value = JsonPrimitive(index.toString())
            }

            is JsonObject -> {
                textFieldValue = TextFieldValue(
                    formData.options.values.elementAt(index).jsonPrimitive.content
                )
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

    // Workaround to let the dropdown have the same size as the TextField
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Used to clear the focus on select
    val focusManager = LocalFocusManager.current

    LabelledContainer(label = formData.label) {
        FormTextField(
            readOnly = true,
            value = textFieldValue,
            onValueChange = { },
            onGloballyPositioned = { textFieldSize = it.size.toSize() },
            onFocusChanged = { selectIsExpanded = it.isFocused },
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown, null,
                    tint = XS2ATheme.CURRENT.textColor.value
                )
            }
        )

        DropdownMenu(
            expanded = selectIsExpanded,
            onDismissRequest = {
                focusManager.clearFocus()
            },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .background(
                    XS2ATheme.CURRENT.surfaceColor.value,
                    XS2ATheme.CURRENT.inputShape.value,
                )
        ) {
            val optionsArray = when (formData.options) {
                is JsonArray -> formData.options.toList()
                is JsonObject -> formData.options.values.toList()
                else -> throw IllegalArgumentException()
            }

            optionsArray.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        setValue(index)
                        focusManager.clearFocus()
                    },
                    text = {
                        Column(
                            modifier = Modifier.padding(2.dp, 4.dp)
                        ) {
                            FormText(
                                text = item.jsonPrimitive.content,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                            )
                        }
                    })
            }
        }
    }
}