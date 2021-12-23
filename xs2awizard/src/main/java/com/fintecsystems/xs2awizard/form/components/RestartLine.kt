package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.RestartLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormButton

/**
 * Renders a button to perform restart-action.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun RestartLine(formData: RestartLineData, viewModel: XS2AWizardViewModel) {
    FormButton(label = formData.label!!, buttonStyle = XS2ATheme.CURRENT.restartButtonStyle) {
        viewModel.restart()
    }
}