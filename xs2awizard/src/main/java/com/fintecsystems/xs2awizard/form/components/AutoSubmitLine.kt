package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.AutoSubmitLineData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoSubmitLine(formData: AutoSubmitLineData, viewModel: XS2AWizardViewModel) {
    LaunchedEffect(null) {
        launch {
            delay(formData.interval.toLong())

            viewModel.submitForm(viewModel.constructJsonBody("autosubmit"), false)
        }
    }
}