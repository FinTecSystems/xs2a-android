package com.fintecsystems.xs2awizard.form.components

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.DescriptionLineData
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard.helper.Utils.getActivity

/**
 * Displays an description text
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun DescriptionLine(formData: DescriptionLineData, viewModel: XS2AWizardViewModel) {
    if (formData.text?.isNotEmpty() == true) {
        val annotatedString = MarkupParser.parseMarkupText(formData.text)
        val activity = LocalContext.current.getActivity<Activity>()

        val localFocusManager = LocalFocusManager.current

        ClickableText(
            modifier = Modifier.fillMaxWidth(),
            text = annotatedString,
            style = MaterialTheme.typography.body2,
            onClick = {
                annotatedString.getStringAnnotations(it, it)
                    .firstOrNull()?.let { annotation ->
                        viewModel.handleAnnotationClick(activity!!, annotation)
                        localFocusManager.clearFocus()
                    }
            }
        )
    }
}