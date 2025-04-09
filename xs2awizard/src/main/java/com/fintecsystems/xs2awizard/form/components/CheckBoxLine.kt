package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalRippleConfiguration
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
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
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckBoxLine(formData: CheckBoxLineData, viewModel: XS2AWizardViewModel) {
    var checkBoxValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.boolean ?: false
        )
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val localFocusManager = LocalFocusManager.current

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

        localFocusManager.clearFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset((-14).dp, 0.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = enabled,
                onClick = {
                    onCheckedChange(!checkBoxValue)
                }),
    ) {

        CompositionLocalProvider(LocalRippleConfiguration provides null) {
            Checkbox(
                checked = checkBoxValue,
                onCheckedChange = ::onCheckedChange,
                interactionSource = interactionSource,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = XS2ATheme.CURRENT.tintColor.value,
                    uncheckedColor = XS2ATheme.CURRENT.unselectedColor.value,
                    checkmarkColor = XS2ATheme.CURRENT.onTintColor.value
                )
            )
        }

        if (!formData.label.isNullOrEmpty()) {
            val parseResult = MarkupParser.parseMarkupText(formData.label)

            FormText(
                modifier = Modifier
                    .clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            onCheckedChange(!checkBoxValue)
                        }
                    ),
                parseResult = parseResult,
                style = MaterialTheme.typography.body2.copy(
                    color = if (enabled) XS2ATheme.CURRENT.textColor.value else XS2ATheme.CURRENT.disabledColor.value
                ),
                onLinkAnnotationClick = viewModel::handleLinkAnnotationClick
            )
        }
    }
}
