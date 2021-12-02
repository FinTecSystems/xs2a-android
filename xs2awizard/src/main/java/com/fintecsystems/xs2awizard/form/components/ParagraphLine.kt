package com.fintecsystems.xs2awizard.form.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.fintecsystems.xs2awizard.components.MarkupParser
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.ParagraphLineData

@Composable
fun ParagraphLine(formData: ParagraphLineData) {
    val textColor = when (formData.severity) {
        "info" -> XS2ATheme.CURRENT.infoParagraphStyle.textColor
        "error" -> XS2ATheme.CURRENT.errorParagraphStyle.textColor
        "warning" -> XS2ATheme.CURRENT.warningParagraphStyle.textColor
        else -> XS2ATheme.CURRENT.textColor
    }

    val backgroundColor = when (formData.severity) {
        "info" -> XS2ATheme.CURRENT.infoParagraphStyle.backgroundColor
        "error" -> XS2ATheme.CURRENT.errorParagraphStyle.backgroundColor
        "warning" -> XS2ATheme.CURRENT.warningParagraphStyle.backgroundColor
        else -> XS2ATheme.CURRENT.backgroundColor
    }

    val padding = XS2ATheme.CURRENT.paragraphPadding
    val margin = XS2ATheme.CURRENT.paragraphMargin

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                margin.start,
                margin.top,
                margin.end,
                margin.bottom
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    padding.start,
                    padding.top,
                    padding.end,
                    padding.bottom
                )
                .background(
                    color = backgroundColor,
                    shape = XS2ATheme.CURRENT.paragraphShape
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (formData.title?.isNotEmpty() == true) {
                Text(
                    text = formData.title,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            if (formData.text.isNotEmpty()) {
                val annotatedString = MarkupParser.parseMarkupText(formData.text)

                ClickableText(
                    text = annotatedString,
                    style = TextStyle(
                        color = textColor,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                    ),
                    onClick = {
                        annotatedString.getStringAnnotations(it, it)
                            .firstOrNull()?.let { annotation ->
                                // TODO: Add logic
                                Log.d(
                                    "XS2AWizard",
                                    "ParagraphLine: Clicked Annotation ${annotation.item}"
                                )
                            }
                    }
                )
            }
        }
    }
}