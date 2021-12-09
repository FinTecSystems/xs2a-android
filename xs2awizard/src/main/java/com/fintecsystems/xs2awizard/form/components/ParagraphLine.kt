package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.PaddingMarginConfiguration
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard.helper.Utils.getActivity

@Composable
fun ParagraphLine(formData: ParagraphLineData, viewModel: XS2AWizardViewModel) {
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

    val isAlert = formData.severity != "info"
            && formData.severity != "error"
            && formData.severity != "warning"

    val padding =
        if (isAlert) XS2ATheme.CURRENT.paragraphPadding else PaddingMarginConfiguration.None
    val margin =
        if (isAlert) XS2ATheme.CURRENT.paragraphMargin else PaddingMarginConfiguration.None

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
                .background(
                    color = backgroundColor,
                    shape = XS2ATheme.CURRENT.paragraphShape
                )
                .padding(
                    padding.start,
                    padding.top,
                    padding.end,
                    padding.bottom
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (formData.title?.isNotEmpty() == true) {
                FormText(
                    text = formData.title,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            if (formData.text.isNotEmpty()) {
                val annotatedString = MarkupParser.parseMarkupText(formData.text)
                val activity = LocalContext.current.getActivity()

                ClickableText(
                    text = annotatedString,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 17.sp,
                    ),
                    onClick = {
                        annotatedString.getStringAnnotations(it, it)
                            .firstOrNull()?.let { annotation ->
                                viewModel.handleAnnotationClick(activity!!, annotation)
                            }
                    }
                )
            }
        }
    }
}