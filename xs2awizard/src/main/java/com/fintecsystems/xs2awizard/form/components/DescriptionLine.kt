package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
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
    Column(
        modifier = Modifier.fillMaxWidth()
            .semantics(true) {} // FIXME: If we don't do this and there is a LinkAnnotation within the text,
                                //        the focus will be all over the place. If we find a better solution for this,
                                //        then we can remove it.
    ) {
        if (formData.text?.isNotEmpty() == true) {
            val parseResult = MarkupParser.parseMarkupText(formData.text)
            val localFocusManager = LocalFocusManager.current

            FormText(
                modifier = Modifier.fillMaxWidth(),
                parseResult = parseResult,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                onLinkAnnotationClick = {
                    viewModel.handleLinkAnnotationClick(it)
                    localFocusManager.clearFocus()
                }
            )
        }
    }
}