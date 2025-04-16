package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.components.theme.styles.SizeConstraint

/**
 * Basic button used in the form.
 *
 * @param label Text on the Button.
 * @param buttonStyle Style to apply.
 * @param onClick Callback, when the button is clicked.
 */
@Composable
fun FormButton(
    label: String,
    buttonStyle: ButtonStyle,
    onClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = XS2ATheme.CURRENT.buttonHorizontalAlignment.value
    ) {
        Button(
            shape = XS2ATheme.CURRENT.buttonShape.value,
            border = BorderStroke(
                buttonStyle.borderStyle.width,
                buttonStyle.borderStyle.color.value
            ),
            modifier = Modifier
                .then(
                    when (XS2ATheme.CURRENT.buttonSize) {
                        is SizeConstraint.FillMaxWidth -> Modifier.fillMaxWidth()
                        is SizeConstraint.WrapContent -> Modifier
                        is SizeConstraint.Size -> Modifier.size(
                            (XS2ATheme.CURRENT.buttonSize as SizeConstraint.Size).width,
                            (XS2ATheme.CURRENT.buttonSize as SizeConstraint.Size).height,
                        )
                    }
                ),
            onClick = {
                focusManager.clearFocus()
                onClick()
            },
            colors = ButtonDefaults.buttonColors(containerColor = buttonStyle.backgroundColor.value)
        ) {
            FormText(
                text = label,
                color = buttonStyle.textColor.value
            )
        }
    }
}