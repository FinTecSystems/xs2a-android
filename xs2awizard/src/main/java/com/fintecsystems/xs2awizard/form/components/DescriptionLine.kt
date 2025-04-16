package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.DescriptionLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.helper.MarkupParser

/**
 * Displays an description text
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun DescriptionLine(formData: DescriptionLineData, viewModel: XS2AWizardViewModel) {
    if (formData.text?.isNotEmpty() == true) {
        val parseResult = MarkupParser.parseMarkupText(formData.text)
        val localFocusManager = LocalFocusManager.current

        FormText(
            modifier = Modifier.fillMaxWidth(),
            parseResult = parseResult,
            style = MaterialTheme.typography.bodyMedium,
            onLinkAnnotationClick = {
                viewModel.handleLinkAnnotationClick(it)
                localFocusManager.clearFocus()
            }
        )
    }
}