package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

@Composable
fun FormTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onFocusChanged: (FocusState) -> Unit = {},
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        placeholder = {
            if (placeholder != null)
                FormText(
                    text = placeholder,
                    color = XS2ATheme.CURRENT.textColor,
                )
        },
        shape = XS2ATheme.CURRENT.inputShape,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged(onFocusChanged)
            .onGloballyPositioned(onGloballyPositioned),
        colors = TextFieldDefaults.textFieldColors(
            textColor = XS2ATheme.CURRENT.inputTextColor,
            backgroundColor = XS2ATheme.CURRENT.inputBackgroundColor,
            focusedIndicatorColor = XS2ATheme.CURRENT.tintColor,
            cursorColor = XS2ATheme.CURRENT.tintColor,
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
    )
}