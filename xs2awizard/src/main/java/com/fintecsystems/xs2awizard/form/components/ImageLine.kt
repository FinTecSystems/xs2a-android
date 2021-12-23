package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.ImageLineData
import com.fintecsystems.xs2awizard.helper.Utils

/**
 * Displays a Image.
 *
 * @param formData Data of this FormLine
 */
@Composable
fun ImageLine(formData: ImageLineData) {
    val imageBitmap = Utils.decodeBase64Image(formData.data)

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = stringResource(id = R.string.image_description),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(minHeight = 100.dp)
                .fillMaxSize()
        )
    }
}