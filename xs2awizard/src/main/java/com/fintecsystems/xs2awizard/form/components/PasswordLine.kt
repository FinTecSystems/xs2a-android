package com.fintecsystems.xs2awizard.form.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.fintecsystems.xs2awizard.form.PasswordLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Renders an Password-TextField.
 *
 * @param formData Data of this FormLine
 */
@Composable
fun PasswordLine(formData: PasswordLineData) {
    var textFieldValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.content ?: ""
        )
    }

    /**
     * Callback for when value of the TextField changed.
     *
     * @param newValue
     */
    fun onValueChange(newValue: String) {
        textFieldValue = newValue
        // Update formData.value as well
        formData.value = JsonPrimitive(newValue)
    }

    LabelledContainer(
        label = formData.label,
        required = formData.required
    ) {
        // TODO: Migrated to SecureTextField/OutlinedSecureTextField
        FormTextField(
            value = textFieldValue,
            onValueChange = ::onValueChange,
            placeholder = formData.placeholder,
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            isError = formData.invalid,
            required = formData.required,
            errorMessage = formData.validationError
        )
    }
}