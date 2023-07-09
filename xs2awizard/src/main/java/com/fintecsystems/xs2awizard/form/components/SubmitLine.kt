package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.SubmitLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormButton

/**
 * Renders a button to perform submit-action.
 *
 * Can optionally render a Back-Button as well.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun SubmitLine(formData: SubmitLineData, viewModel: XS2AWizardViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        FormButton(label = formData.label!!, buttonStyle = XS2ATheme.CURRENT.submitButtonStyle) {
            viewModel.submitForm()
        }

        if (viewModel.enableBackButton && formData.backLabel != null) {
            FormButton(
                label = formData.backLabel,
                buttonStyle = XS2ATheme.CURRENT.backButtonStyle
            ) {
                viewModel.goBack()
            }
        }
    }
}