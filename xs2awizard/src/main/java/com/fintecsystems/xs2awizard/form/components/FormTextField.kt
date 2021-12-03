package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = {
            if (placeholder != null)
                Text(
                    text = placeholder,
                    style = TextStyle(
                        color = XS2ATheme.CURRENT.textColor,
                    )
                )
        },
        shape = XS2ATheme.CURRENT.inputShape,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            textColor = XS2ATheme.CURRENT.inputTextColor,
            backgroundColor = XS2ATheme.CURRENT.inputBackgroundColor
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
    )
}