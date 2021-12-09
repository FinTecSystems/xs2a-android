package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

@Composable
fun FormText(
    text: String,
    color: Color = XS2ATheme.CURRENT.textColor,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        maxLines = maxLines
    )
}