package com.fintecsystems.xs2awizard.form.components

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.DescriptionLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
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
        val parseResult = MarkupParser.parseMarkupText(formData.text)
        val activity = LocalContext.current.getActivity<Activity>()

        val localFocusManager = LocalFocusManager.current

        FormText(
            modifier = Modifier.fillMaxWidth(),
            parseResult = parseResult,
            style = MaterialTheme.typography.body2,
            onClick = {
                if (it != null) {
                    viewModel.handleLinkAnnotationClick(activity!!, it)
                    localFocusManager.clearFocus()
                }
            }
        )
    }
}