package com.fintecsystems.xs2awizard.form.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.PaddingMarginConfiguration
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard.helper.Utils.getActivity

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

    val isAlert = formData.severity == "info"
            || formData.severity == "error"
            || formData.severity == "warning"

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
                    color = backgroundColor.value,
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
                    color = textColor.value,
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            if (formData.text.isNotEmpty()) {
                val parseResult = MarkupParser.parseMarkupText(formData.text)
                val activity = LocalContext.current.getActivity<Activity>()

                val localFocusManager = LocalFocusManager.current

                FormText(
                    parseResult = parseResult,
                    style = MaterialTheme.typography.body1.copy(
                        color = textColor.value
                    ),
                    onClick = {
                        if (it != null) {
                            viewModel.handleLinkAnnotationClick(activity!!, it)
                            localFocusManager.clearFocus()
                        }
                    }
                )
            }
        }
    }
}