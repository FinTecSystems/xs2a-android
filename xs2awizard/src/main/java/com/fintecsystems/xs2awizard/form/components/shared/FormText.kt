package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

/**
 * Pre-Styled Text used for all FormLines.
 *
 * Inherits props from [Text]
 */
@Composable
fun FormText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = XS2ATheme.CURRENT.textColor,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        maxLines = maxLines,
        textAlign = textAlign,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis
    )
}