package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.SelectLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import kotlinx.serialization.json.*

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
                formData.value = JsonPrimitive(index)
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
                Icon(Icons.Filled.ArrowDropDown, null)
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
                    XS2ATheme.CURRENT.surfaceColor,
                    XS2ATheme.CURRENT.inputShape,
                )
        ) {
            val optionsArray = when (formData.options) {
                is JsonArray -> formData.options.toList()
                is JsonObject -> formData.options.values.toList()
                else -> throw IllegalArgumentException()
            }

            optionsArray.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    setValue(index)
                    focusManager.clearFocus()
                }) {
                    Column(
                        modifier = Modifier.padding(2.dp, 4.dp)
                    ) {
                        FormText(
                            text = item.jsonPrimitive.content,
                            color = XS2ATheme.CURRENT.textColor,
                            fontSize = 17.sp,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}