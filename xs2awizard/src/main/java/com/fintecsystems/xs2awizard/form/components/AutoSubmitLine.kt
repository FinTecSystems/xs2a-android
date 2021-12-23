package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.AutoSubmitLineData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Renders an ProgressIndicator and performs autosubmit-action after delay.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun AutoSubmitLine(formData: AutoSubmitLineData, viewModel: XS2AWizardViewModel) {
    LaunchedEffect(null) {
        launch {
            delay(formData.interval.toLong())

            viewModel.submitForm(viewModel.constructJsonBody("autosubmit"), false)
        }
    }

    Box(Modifier.fillMaxWidth()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = XS2ATheme.CURRENT.tintColor
        )
    }
}