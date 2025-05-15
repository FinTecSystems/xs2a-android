package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.helper.MarkupParser
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

/**
 * Displays a CheckBox with a description text.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckBoxLine(formData: CheckBoxLineData, viewModel: XS2AWizardViewModel) {
    var checkBoxValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.boolean == true
        )
    }

    val enabled = formData.disabled == false

    /**
     * Callback for when value of the TextField changed.
     *
     * @param newValue
     */
    fun onCheckedChange(newValue: Boolean) {
        checkBoxValue = newValue
        // Update formData.value as well
        formData.value = JsonPrimitive(newValue)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checkBoxValue,
                enabled = enabled,
                onValueChange = { onCheckedChange(!checkBoxValue) },
                role = Role.Checkbox
            ),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Checkbox(
            checked = checkBoxValue,
            onCheckedChange = null,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                uncheckedColor = if (formData.invalid) MaterialTheme.colorScheme.error
                else Color.Unspecified,
            )
        )

        Column {
            if (!formData.label.isNullOrEmpty()) {
                val parseResult = MarkupParser.parseMarkupText(formData.label)

                FormText(
                    parseResult = parseResult,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (enabled) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onSurface
                            .copy(alpha = 0.38f)
                    ),
                    onLinkAnnotationClick = viewModel::handleLinkAnnotationClick
                )
            }

            if (formData.required || !formData.validationError.isNullOrEmpty()) {
                val supportText =
                    formData.validationError ?: stringResource(R.string.input_required_label)
                FormText(
                    text = supportText,
                    color = if (formData.invalid) MaterialTheme.colorScheme.error else Color.Unspecified,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    }
}
