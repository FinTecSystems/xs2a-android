package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle

@Composable
fun FormButton(
    label: String,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
) {
    Button(
        shape = XS2ATheme.CURRENT.buttonShape,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonStyle.backgroundColor)
    ) {
        Text(
            text = label,
            color = buttonStyle.textColor
        )
    }
}