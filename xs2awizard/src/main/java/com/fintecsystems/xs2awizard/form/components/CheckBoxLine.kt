package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard.helper.Utils.getActivity
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun CheckBoxLine(formData: CheckBoxLineData, viewModel: XS2AWizardViewModel) {
    var checkBoxValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.boolean ?: false
        )
    }

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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),

    ) {
        Checkbox(
            checked = checkBoxValue,
            onCheckedChange = ::onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = XS2ATheme.CURRENT.tintColor
            )
        )

        if (!formData.label.isNullOrEmpty()) {
            val annotatedString = MarkupParser.parseMarkupText(formData.label)
            val activity = LocalContext.current.getActivity()

            ClickableText(
                text = annotatedString,
                style = TextStyle(
                    color = XS2ATheme.CURRENT.textColor,
                ),
                onClick = {
                    annotatedString.getStringAnnotations(it, it)
                        .firstOrNull()?.let { annotation ->
                            viewModel.handleAnnotationClick(activity!!, annotation)
                        }
                }
            )
        }
    }
}