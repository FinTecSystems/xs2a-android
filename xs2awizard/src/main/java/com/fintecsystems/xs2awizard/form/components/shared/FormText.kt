package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.fintecsystems.xs2awizard.helper.MarkupParser

/**
 * Pre-Styled Text used for all FormLines.
 *
 * Inherits props from [Text]
 */
@Composable
fun FormText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        maxLines = maxLines,
        textAlign = textAlign,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        style = style
    )
}

/**
 * Pre-Styled Text used for all FormLines.
 *
 * Inherits props from [Text]
 */
@Composable
fun FormText(
    modifier: Modifier = Modifier,
    parseResult: MarkupParser.ParseResult,
    style: TextStyle = LocalTextStyle.current,
    onLinkAnnotationClick: (LinkAnnotation.Clickable) -> Unit = {}
) {
    val linkInteractionListener = remember {
        LinkInteractionListener {
            if (it is LinkAnnotation.Clickable) {
                onLinkAnnotationClick(it)
            }
        }
    }
    val annotatedString = MarkupParser.parseParseResult(parseResult, linkInteractionListener)

    Text(
        modifier = modifier,
        text = annotatedString,
        style = style,
    )
}