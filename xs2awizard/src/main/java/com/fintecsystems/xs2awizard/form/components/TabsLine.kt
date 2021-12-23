package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.TabsLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormTabs
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Shows a Row of Tabs.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun TabsLine(formData: TabsLineData, viewModel: XS2AWizardViewModel) {
    val selectedTabIndex = formData.tabs.keys.indexOf(formData.selected)

    val focusManager = LocalFocusManager.current

    FormTabs(
        selected = selectedTabIndex,
        onSelectedChange = {
            focusManager.clearFocus()

            viewModel.submitForm(
                buildJsonObject {
                    put("action", formData.action)
                    put("params", formData.tabs.keys.elementAt(it))
                },
                true
            )
        },
        tabs = formData.tabs.values.toList()
    )
}
