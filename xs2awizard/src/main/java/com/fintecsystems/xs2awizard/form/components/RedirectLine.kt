package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.RedirectLineData

@Composable
fun RedirectLine(formData: RedirectLineData, viewModel: XS2AWizardViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FormButton(label = formData.label!!, buttonStyle = XS2ATheme.CURRENT.redirectButtonStyle) {
            viewModel.openWebView(formData.url!!)
        }

        FormButton(
            label = formData.backLabel!!,
            buttonStyle = XS2ATheme.CURRENT.backButtonStyle
        ) {
            viewModel.goBack()
        }
    }
}