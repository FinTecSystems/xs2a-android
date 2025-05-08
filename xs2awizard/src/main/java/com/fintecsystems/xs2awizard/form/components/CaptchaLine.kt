package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.form.CaptchaLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import com.fintecsystems.xs2awizard.helper.Utils
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Displays an Captcha-Element with a [FormTextField] below it.
 *
 * @param formData Data of this FormLine
 */
@Composable
fun CaptchaLine(formData: CaptchaLineData) {
    val imageBitmap = Utils.decodeBase64Image(formData.data)

    var textFieldValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.content ?: ""
        )
    }

    fun onValueChange(newValue: String) {
        textFieldValue = newValue
        formData.value = JsonPrimitive(newValue)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = formData.description,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        FormTextField(
            value = textFieldValue,
            onValueChange = ::onValueChange,
            placeholder = formData.placeholder,
            isError = formData.invalid,
            required = formData.required,
            errorMessage = formData.validationError,
            label = formData.label
        )
    }
}