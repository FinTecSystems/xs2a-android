package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.ImageLineData
import com.fintecsystems.xs2awizard.helper.Utils

@Composable
fun ImageLine(formData: ImageLineData) {
    val imageBitmap = Utils.decodeBase64Image(formData.data)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = stringResource(id = R.string.image_description),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}