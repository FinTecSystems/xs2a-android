package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.AbortLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormButton

/**
 * Shows an Abort-Button
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun AbortLine(formData: AbortLineData, viewModel: XS2AWizardViewModel) {
    FormButton(label = formData.label!!, buttonStyle = XS2ATheme.CURRENT.abortButtonStyle) {
        viewModel.abort()
    }
}