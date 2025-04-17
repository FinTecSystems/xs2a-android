package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.TextFieldType

/**
 * Pre-Styled Text used for all FormLines, with built-in focus changer.
 *
 * Inherits props from [TextField]
 */
@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String? = null,
    label: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFocusChanged: (FocusState) -> Unit = {},
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    RelevantTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged(onFocusChanged)
            .onGloballyPositioned(onGloballyPositioned),
        textFieldType = XS2ATheme.CURRENT.inputType,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = {
            if (placeholder != null)
                FormText(
                    text = placeholder,
                    color = XS2ATheme.CURRENT.textColor.value,
                )
        },
        label = {
            if (label != null)
                FormText(
                    text = label,
                    color = XS2ATheme.CURRENT.textColor.value,
                )
        },
        shape = XS2ATheme.CURRENT.inputShape.value,
        colors = TextFieldDefaults.colors(
            focusedTextColor = XS2ATheme.CURRENT.inputTextColor.value,
            unfocusedTextColor = XS2ATheme.CURRENT.inputTextColor.value,
            focusedContainerColor = XS2ATheme.CURRENT.inputBackgroundColor.value,
            unfocusedContainerColor = XS2ATheme.CURRENT.inputBackgroundColor.value,
            focusedIndicatorColor = XS2ATheme.CURRENT.tintColor.value,
            unfocusedIndicatorColor = XS2ATheme.CURRENT.tintColor.value,
            cursorColor = XS2ATheme.CURRENT.tintColor.value,
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (!focusManager.moveFocus(FocusDirection.Down)) {
                    focusManager.clearFocus()
                }
             },
        ),
        trailingIcon = trailingIcon,
        singleLine = singleLine,
    )
}

@Composable
private fun RelevantTextField(
    textFieldType: TextFieldType,
    modifier: Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: @Composable () -> Unit,
    label: @Composable () -> Unit,
    enabled: Boolean,
    readOnly: Boolean,
    singleLine: Boolean,
    visualTransformation: VisualTransformation,
    trailingIcon: @Composable () -> Unit,
    colors: TextFieldColors,
    shape: Shape,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
) = when(textFieldType) {
    TextFieldType.OUTLINED -> OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = placeholder,
        label = label,
        shape = shape,
        modifier = modifier,
        colors = colors,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
    )
    TextFieldType.NORMAL -> TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = placeholder,
        label = label,
        shape = shape,
        modifier = modifier,
        colors = colors,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
    )
}
