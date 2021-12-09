package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

@Composable
fun LabelledContainer(label: String?, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (label != null) {
            Text(
                text = label,
                color = XS2ATheme.CURRENT.textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        content()
    }
}