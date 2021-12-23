package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.FormLines
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.MultiLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormTabs

/**
 * Renders multiple sub-forms, which are bundled into tabs.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun MultiLine(
    formData: MultiLineData,
    viewModel: XS2AWizardViewModel
) {
    val tabs = formData.forms.associate { it.value to it.label }
    var selectedTabIndex by remember {
        mutableStateOf(
            tabs.keys.indexOf(formData.selected)
        )
    }

    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        FormTabs(
            selected = selectedTabIndex,
            onSelectedChange = {
                focusManager.clearFocus()

                selectedTabIndex = it
                formData.selected = formData.forms[it].value
            },
            tabs = tabs.values.toList()
        )

        FormLines(formData = formData.forms[selectedTabIndex].form, viewModel = viewModel)
    }
}
