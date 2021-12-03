package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.*
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.form.TextLineData
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun TextLine(formData: TextLineData, viewModel: XS2AWizardViewModel) {
    var value by remember { mutableStateOf(formData.value?.jsonPrimitive?.content ?: "") }

    LabelledContainer(label = formData.label) {
        FormTextField(
            value = value,
            onValueChange = {
                if (formData.maxLength == null || it.length < formData.maxLength) {
                    value = it
                    formData.value = JsonPrimitive(it)
                }
            },
            placeholder = formData.placeholder
        )
    }
}