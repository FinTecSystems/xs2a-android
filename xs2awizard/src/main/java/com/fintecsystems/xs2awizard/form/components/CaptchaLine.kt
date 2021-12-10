package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.CaptchaLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import com.fintecsystems.xs2awizard.helper.Utils
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun CaptchaLine(formData: CaptchaLineData) {
    val imageBitmap = Utils.decodeBase64Image(formData.data)

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                formData.value?.jsonPrimitive?.content ?: ""
            )
        )
    }

    fun onValueChange(newValue: TextFieldValue) {
        textFieldValue = newValue
        formData.value = JsonPrimitive(newValue.text)
    }

    LabelledContainer(formData.label) {
        Image(
            bitmap = imageBitmap,
            contentDescription = stringResource(id = R.string.captcha_image_description),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        FormTextField(
            value = textFieldValue,
            onValueChange = ::onValueChange,
            placeholder = formData.placeholder
        )
    }
}