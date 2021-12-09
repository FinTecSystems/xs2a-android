package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle

@Composable
fun FormButton(
    label: String,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Button(
        shape = XS2ATheme.CURRENT.buttonShape,
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            focusManager.clearFocus()
            onClick()
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonStyle.backgroundColor)
    ) {
        FormText(
            text = label,
            color = buttonStyle.textColor
        )
    }
}