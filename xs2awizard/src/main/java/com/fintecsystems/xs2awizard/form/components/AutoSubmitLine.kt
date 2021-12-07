package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.AutoSubmitLineData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoSubmitLine(formData: AutoSubmitLineData, viewModel: XS2AWizardViewModel) {
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(null) {
        coroutineScope.launch {
            delay(formData.interval.toLong())

            viewModel.submitForm(viewModel.constructJsonBody("autosubmit"), false)
        }

        onDispose { /* no-op */ }
    }
}