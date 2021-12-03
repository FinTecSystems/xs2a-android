package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.Composable
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.RestartLineData

@Composable
fun RestartLine(formData: RestartLineData, viewModel: XS2AWizardViewModel) {
    FormButton(label = formData.label!!, buttonStyle = XS2ATheme.CURRENT.restartButtonStyle) {
        viewModel.restart()
    }
}