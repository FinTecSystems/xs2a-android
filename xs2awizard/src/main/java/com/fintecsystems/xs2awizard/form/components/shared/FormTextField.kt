package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.fintecsystems.xs2awizard.R
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
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    label: String? = null,
    errorMessage: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFocusChanged: (FocusState) -> Unit = {},
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
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
        placeholder = if (placeholder != null) {
            @Composable {
                FormText(
                    text = placeholder,
                    color = XS2ATheme.CURRENT.textColor.value,
                )
            }
        } else null,
        label = if (label != null) {
            @Composable {
                FormText(
                    text = label + if (required) "*" else "",
                    color = XS2ATheme.CURRENT.textColor.value,
                )
            }
        } else null,
        supportingText = if (required || errorMessage != null) {
            @Composable {
                FormText(
                    text = errorMessage ?: stringResource(R.string.input_required_label),
                    color = if (isError) XS2ATheme.CURRENT.errorColor.value
                    else XS2ATheme.CURRENT.inputTextColor.value,
                )
            }
        } else null,
        isError = isError,
        shape = XS2ATheme.CURRENT.inputShape.value,
        colors = TextFieldDefaults.colors(
            focusedTextColor = XS2ATheme.CURRENT.inputTextColor.value,
            unfocusedTextColor = XS2ATheme.CURRENT.inputTextColor.value,
            focusedContainerColor = XS2ATheme.CURRENT.inputBackgroundColor.value,
            unfocusedContainerColor = XS2ATheme.CURRENT.inputBackgroundColor.value,
            focusedIndicatorColor = XS2ATheme.CURRENT.tintColor.value,
            unfocusedIndicatorColor = XS2ATheme.CURRENT.tintColor.value,
            cursorColor = XS2ATheme.CURRENT.tintColor.value,
            errorTextColor = XS2ATheme.CURRENT.errorColor.value,
            errorIndicatorColor = XS2ATheme.CURRENT.errorColor.value,
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, // TODO: Add some way to identify the last TextInput of a form
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
        interactionSource = interactionSource
    )
}

@Composable
private fun RelevantTextField(
    textFieldType: TextFieldType,
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: (@Composable () -> Unit)? = null,
    label: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean,
    readOnly: Boolean,
    singleLine: Boolean,
    visualTransformation: VisualTransformation,
    trailingIcon: @Composable () -> Unit,
    colors: TextFieldColors,
    shape: Shape,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    interactionSource: MutableInteractionSource? = null,
) = when (textFieldType) {
    TextFieldType.OUTLINED -> OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = placeholder,
        supportingText = supportingText,
        isError = isError,
        label = label,
        shape = shape,
        modifier = modifier,
        colors = colors,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        interactionSource = interactionSource
    )

    TextFieldType.NORMAL -> TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = placeholder,
        supportingText = supportingText,
        isError = isError,
        label = label,
        shape = shape,
        modifier = modifier,
        colors = colors,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        interactionSource = interactionSource
    )
}
