package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = formData.description,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier
                .defaultMinSize(minHeight = 200.dp, minWidth = 200.dp)
        )
    }
}