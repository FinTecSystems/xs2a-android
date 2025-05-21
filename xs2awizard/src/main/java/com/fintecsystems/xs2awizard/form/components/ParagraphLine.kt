package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.PaddingMarginConfiguration
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.helper.MarkupParser

/**
 * Shows an title and description text.
 *
 * Can be elevated into an info-/error- or warning-alert.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun ParagraphLine(formData: ParagraphLineData, viewModel: XS2AWizardViewModel) {
    val paragraphStyleToUse = when (formData.severity) {
        "info" -> XS2ATheme.CURRENT.infoParagraphStyle
        "error" -> XS2ATheme.CURRENT.errorParagraphStyle
        "warning" -> XS2ATheme.CURRENT.warningParagraphStyle
        else -> null
    }

    val textColor = paragraphStyleToUse?.textColor?.value ?: MaterialTheme.colorScheme.onBackground
    val backgroundColor =
        paragraphStyleToUse?.backgroundColor?.value ?: MaterialTheme.colorScheme.background

    val isAlert = paragraphStyleToUse != null

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
        val stateDescriptionText = when (formData.severity) {
            "info" -> stringResource(R.string.paragraph_severity_info)
            "error" -> stringResource(R.string.paragraph_severity_error)
            "warning" -> stringResource(R.string.paragraph_severity_warning)
            else -> null
        }

        Column(
            modifier = Modifier
                .semantics(true) {
                    if (stateDescriptionText != null) {
                        stateDescription = stateDescriptionText
                    }
                }
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = XS2ATheme.CURRENT.paragraphShape.value
                )
                .padding(
                    padding.start,
                    padding.top,
                    padding.end,
                    padding.bottom
                )
        ) {
            if (formData.title?.isNotEmpty() == true) {
                FormText(
                    text = formData.title,
                    color = textColor,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            if (formData.text.isNotEmpty()) {
                val parseResult = MarkupParser.parseMarkupText(formData.text)
                val localFocusManager = LocalFocusManager.current

                FormText(
                    parseResult = parseResult,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = textColor
                    ),
                    onLinkAnnotationClick = {
                        viewModel.handleLinkAnnotationClick(it)
                        localFocusManager.clearFocus()
                    }
                )
            }
        }
    }
}